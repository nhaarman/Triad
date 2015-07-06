/*
 * Copyright 2013 Square Inc.
 * Copyright 2015 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.triad;

import java.util.Iterator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.nhaarman.triad.Preconditions.checkState;

/**
 * Holds the current truth, the history of screens, and exposes operations to change it.
 */
public class Triad {

  @Nullable
  private Listener mListener;

  @NonNull
  private Backstack mBackstack;

  @Nullable
  private Transition mTransition;

  private Triad(@NonNull final Backstack backstack) {
    mBackstack = backstack;
  }

  private Triad(@NonNull final Backstack backstack, @Nullable final Listener listener) {
    mListener = listener;
    mBackstack = backstack;
  }

  public void setListener(@NonNull final Listener listener) {
    mListener = listener;
  }

  @NonNull
  public Backstack getBackstack() {
    return mBackstack;
  }

  public void startWith(@NonNull final Screen<?, ?, ?> screen) {
    if (mBackstack.size() != 0) {
      return;
    }

    mBackstack = Backstack.single(screen);
  }

  public void goTo(@NonNull final Screen<?, ?, ?> screen) {
    checkState(mBackstack.size() > 0, "Use startWith(Screen) to show your first Screen.");

    move(new GoToTransition(screen));
  }

  public void popTo(@NonNull final Screen<?, ?, ?> screen) {
    move(new PopToTransition(screen));
  }

  public void replaceWith(@NonNull final Screen<?, ?, ?> screen) {
    move(new ReplaceWithTransition(screen));
  }

  public boolean goBack() {
    boolean canGoBack = mBackstack.size() > 1 || mTransition != null && !mTransition.isFinished();

    move(new GoBackTransition());

    return canGoBack;
  }

  public void forward(@NonNull final Backstack newBackstack) {
    move(new ForwardTransition(newBackstack));
  }

  public void backward(@NonNull final Backstack newBackstack) {
    move(new BackwardTransition(newBackstack));
  }

  private void move(@NonNull final Transition transition) {
    if (mTransition == null || mTransition.isFinished()) {
      mTransition = transition;
      transition.execute();
    } else {
      mTransition.enqueue(transition);
    }
  }

  @NonNull
  public static Triad emptyInstance() {
    return new Triad(Backstack.emptyBuilder().build());
  }

  @NonNull
  public static Triad newInstance(@NonNull final Backstack backstack, @NonNull final Listener listener) {
    return new Triad(backstack, listener);
  }

  enum Direction {
    FORWARD, BACKWARD, REPLACE
  }

  /**
   * Supplied by Triad to the Listener, which is responsible for calling onComplete().
   */
  interface Callback {

    /**
     * Must be called exactly once to indicate that the corresponding transition has completed.
     * <p/>
     * If not called, the backstack will not be updated and further calls to Triad will not execute.
     * Calling more than once will result in an exception.
     */
    void onComplete();
  }

  interface Listener {

    /**
     * Notifies the listener that the backstack is about to change. Note that the backstack of
     * is not actually changed until the callback is triggered.  That is, {@code nextBackstack} is
     * where the Triad is going next, and {@link Triad#getBackstack()} is where it's coming from.
     *
     * @param callback Must be called to indicate completion.
     */
    void go(Backstack nextBackstack, Direction direction, Callback callback);
  }

  private abstract class Transition implements Callback {

    private boolean mFinished;

    @Nullable
    private Transition mNext;

    @Nullable
    private Backstack mNextBackstack;

    void enqueue(@NonNull final Transition transition) {
      if (mNext == null) {
        mNext = transition;
      } else {
        mNext.enqueue(transition);
      }
    }

    @Override
    public void onComplete() {
      checkState(!mFinished, "onComplete already called for this transition");

      if (mNextBackstack != null) {
        mBackstack = mNextBackstack;
      }

      mFinished = true;
      if (mNext != null) {
        mTransition = mNext;
        mTransition.execute();
      }
    }

    protected void go(final Backstack nextBackstack, final Direction direction) {
      checkState(mListener != null, "Listener is null. Be sure to call setListener(Listener).");

      mNextBackstack = nextBackstack;
      mListener.go(nextBackstack, direction, this);
    }

    protected abstract void execute();

    public boolean isFinished() {
      return mFinished;
    }
  }

  private class GoBackTransition extends Transition {

    @Override
    public void execute() {
      if (mBackstack.size() == 1) {
        // We are not calling the listener, so we must complete this noop transition ourselves.
        onComplete();
      } else {
        Backstack.Builder builder = mBackstack.buildUpon();
        builder.pop();
        Backstack newBackstack = builder.build();
        go(newBackstack, Direction.BACKWARD);
      }
    }
  }

  private class ReplaceWithTransition extends Transition {

    @NonNull
    private final Screen<?, ?, ?> mScreen;

    private ReplaceWithTransition(@NonNull final Screen<?, ?, ?> screen) {
      mScreen = screen;
    }

    @Override
    public void execute() {
      Backstack.Builder builder = mBackstack.buildUpon();
      builder.pop();
      builder.push(mScreen);
      Backstack newBackstack = builder.build();

      go(newBackstack, Direction.REPLACE);
    }
  }

  private class ForwardTransition extends Transition {

    @NonNull
    private final Backstack mNewBackstack;

    private ForwardTransition(@NonNull final Backstack newBackstack) {
      mNewBackstack = newBackstack;
    }

    @Override
    public void execute() {
      go(mNewBackstack, Direction.FORWARD);
    }
  }

  private class PopToTransition extends Transition {

    @NonNull
    private final Screen<?, ?, ?> mScreen;

    private PopToTransition(@NonNull final Screen<?, ?, ?> screen) {
      mScreen = screen;
    }

    @Override
    public void execute() {
      Backstack.Builder builder = mBackstack.buildUpon();
      int count = 0;
      // Take care to leave the original screen instance on the stack, if we find it.  This enables
      // some arguably bad behavior on the part of clients, but it's still probably the right thing
      // to do.
      Screen<?, ?, ?> lastPopped = null;
      for (Iterator<Backstack.Entry> it = mBackstack.reverseIterator(); it.hasNext(); ) {
        Backstack.Entry entry = it.next();

        if (entry.getScreen().equals(mScreen)) {
          // Clear up to the target screen.
          for (int i = 0; i < mBackstack.size() - count; i++) {
            lastPopped = builder.pop().getScreen();
          }
          break;
        } else {
          count++;
        }
      }

      Backstack newBackstack;
      if (lastPopped != null) {
        builder.push(lastPopped);
        newBackstack = builder.build();
        go(newBackstack, Direction.BACKWARD);
      } else {
        builder.push(mScreen);
        newBackstack = builder.build();
        go(newBackstack, Direction.FORWARD);
      }
    }
  }

  private class GoToTransition extends Transition {

    @NonNull
    private final Screen<?, ?, ?> mScreen;

    private GoToTransition(@NonNull final Screen<?, ?, ?> screen) {
      mScreen = screen;
    }

    @Override
    public void execute() {
      Backstack newBackstack = mBackstack.buildUpon().push(mScreen).build();
      go(newBackstack, Direction.FORWARD);
    }
  }

  private class BackwardTransition extends Transition {

    @NonNull
    private final Backstack mNewBackstack;

    private BackwardTransition(@NonNull final Backstack newBackstack) {
      mNewBackstack = newBackstack;
    }

    @Override
    public void execute() {
      go(mNewBackstack, Direction.BACKWARD);
    }
  }

  private class StartWithTransition extends Transition {

    @NonNull
    private final Screen<?, ?, ?> mScreen;

    private StartWithTransition(@NonNull final Screen<?, ?, ?> screen) {
      mScreen = screen;
    }

    @Override
    protected void execute() {
      Backstack backstack = Backstack.single(mScreen);
      go(backstack, Direction.FORWARD);
    }
  }
}
