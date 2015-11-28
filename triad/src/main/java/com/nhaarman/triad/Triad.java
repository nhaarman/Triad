/*
 * Copyright 2013 Square Inc.
 * Copyright 2015 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.triad;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.util.Iterator;

import static com.nhaarman.triad.Preconditions.checkNotNull;
import static com.nhaarman.triad.Preconditions.checkState;

/**
 * Holds the current truth, the history of screens, and exposes operations to change it.
 */
public class Triad {

  @NonNull
  private final SparseArray<ActivityResultListener> mActivityResultListeners;

  @SuppressWarnings("rawtypes")
  @Nullable
  private Listener mListener;

  @NonNull
  private Backstack mBackstack;

  @Nullable
  private Transition mTransition;

  @NonNull
  private WeakReference<Activity> mActivity;

  private int mRequestCodeCounter;

  private Triad(@NonNull final Backstack backstack) {
    this(backstack, null);
  }

  private Triad(@NonNull final Backstack backstack, @Nullable final Listener<?> listener) {
    new Exception().printStackTrace();

    mListener = listener;
    mBackstack = backstack;

    mActivityResultListeners = new SparseArray<>();

    mActivity = new WeakReference<>(null);
  }

  /**
   * Sets the current Activity, to be able to start other Activities from the Triad instance.
   */
  void setActivity(@Nullable final Activity activity) {
    mActivity = new WeakReference<>(activity);
  }

  public <T> void setListener(@NonNull final Listener<T> listener) {
    mListener = listener;
  }

  @NonNull
  public Backstack getBackstack() {
    return mBackstack;
  }

  /**
   * Initializes the backstack with given Screen. If the backstack is not empty, this call is ignored.
   * This method must be called before any other backstack operation.
   *
   * @param screen The Screen to start with.
   */
  public void startWith(@NonNull final Screen<?> screen) {
    if (mBackstack.size() == 0 && mTransition == null) {
      move(new StartWithTransition(screen));
    }
  }

  /**
   * Pushes given Screen onto the backstack.
   *
   * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
   *
   * @param screen The screen to push onto the backstack.
   */
  public void goTo(@NonNull final Screen<?> screen) {
    goTo(screen, null);
  }

  /**
   * Pushes given Screen onto the backstack.
   *
   * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
   *
   * @param screen The screen to push onto the backstack.
   */
  public void goTo(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
    checkState(mBackstack.size() > 0 || mTransition != null, "Use startWith(Screen) to show your first Screen.");

    move(new GoToTransition(screen, animator));
  }

  /**
   * Forces a notification of the current screen.
   */
  void showCurrent() {
    move(new ShowTransition());
  }

  /**
   * Pops the backstack until given Screen is found.
   * If the Screen is not found, the Screen is pushed onto the current backstack.
   * Does nothing if the Screen is already on top of the stack.
   *
   * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
   *
   * @param screen The Screen to pop to.
   */
  public void popTo(@NonNull final Screen<?> screen) {
    popTo(screen, null);
  }

  /**
   * Pops the backstack until given Screen is found.
   * If the Screen is not found, the Screen is pushed onto the current backstack.
   * Does nothing if the Screen is already on top of the stack.
   *
   * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
   *
   * @param screen The Screen to pop to.
   */
  public void popTo(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
    checkState(mBackstack.size() > 0 || mTransition != null, "Use startWith(Screen) to show your first Screen.");

    move(new PopToTransition(screen, animator));
  }

  /**
   * Replaces the current Screen with given Screen.
   *
   * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
   *
   * @param screen The Screen to replace the current Screen.
   */
  public void replaceWith(@NonNull final Screen<?> screen) {
    replaceWith(screen, null);
  }

  /**
   * Replaces the current Screen with given Screen.
   *
   * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
   *
   * @param screen The Screen to replace the current Screen.
   */
  public void replaceWith(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
    checkState(mBackstack.size() > 0 || mTransition != null, "Use startWith(Screen) to show your first Screen.");

    move(new ReplaceWithTransition(screen, animator));
  }

  /**
   * Pops the current screen off the backstack.
   * Does nothing if the backstack would be empty afterwards.
   *
   * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
   *
   * @return true if the transition will execute.
   */
  public boolean goBack() {
    checkState(mBackstack.size() > 0 || mTransition != null, "Use startWith(Screen) to show your first Screen.");

    boolean canGoBack = mBackstack.size() > 1 || mTransition != null && !mTransition.isFinished();
    move(new GoBackTransition());
    return canGoBack;
  }

  /**
   * Replaces the entire backstack with given backstack, in a forward manner.
   *
   * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
   *
   * @param newBackstack The new backstack.
   */
  public void forward(@NonNull final Backstack newBackstack) {
    checkState(mBackstack.size() > 0 || mTransition != null, "Use startWith(Screen) to show your first Screen.");

    move(new ForwardTransition(newBackstack));
  }

  /**
   * Replaces the entire backstack with given backstack, in a backward manner.
   *
   * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
   *
   * @param newBackstack The new backstack.
   */
  public void backward(@NonNull final Backstack newBackstack) {
    checkState(mBackstack.size() > 0 || mTransition != null, "Use startWith(Screen) to show your first Screen.");

    move(new BackwardTransition(newBackstack));
  }

  /**
   * Replaces the entire backstack with given backstack, in a replace manner.
   *
   * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
   *
   * @param newBackstack The new backstack.
   */
  public void replace(@NonNull final Backstack newBackstack) {
    checkState(mBackstack.size() > 0 || mTransition != null, "Use startWith(Screen) to show your first Screen.");

    move(new ReplaceTransition(newBackstack));
  }

  /**
   * Launches the Activity described by given Intent.
   *
   * @param intent The Activity to start.
   */
  public void startActivity(@NonNull final Intent intent) {
    checkState(mActivity.get() != null, "Activity reference is null.");

    mActivity.get().startActivity(intent);
  }

  /**
   * Launches the Activity described by given Intent for result.
   *
   * @param intent   The Activity to start for result.
   * @param listener The callback to notify for the result.
   */
  public void startActivityForResult(@NonNull final Intent intent, @NonNull final ActivityResultListener listener) {
    checkState(mActivity.get() != null, "Activity reference is null.");

    mActivity.get().startActivityForResult(intent, mRequestCodeCounter);
    mActivityResultListeners.put(mRequestCodeCounter, listener);
    mRequestCodeCounter++;
  }

  /**
   * Propagates the activity result to the proper {@link ActivityResultListener}.
   *
   * @param requestCode The request code, as created by {@link #startActivityForResult(Intent, ActivityResultListener)}.
   * @param resultCode  The result code of the returning Activity.
   * @param data        The result data of the returning Activity.
   */
  void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
    mActivityResultListeners.get(requestCode).onActivityResult(resultCode, data);
    mActivityResultListeners.remove(requestCode);
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
  public static Triad newInstance(@NonNull final Backstack backstack, @NonNull final Listener<?> listener) {
    return new Triad(backstack, listener);
  }

  /**
   * Supplied by Triad to the Listener, which is responsible for calling onComplete().
   */
  public interface Callback {

    /**
     * Must be called exactly once to indicate that the corresponding transition has completed.
     * <p/>
     * If not called, the backstack will not be updated and further calls to Triad will not execute.
     * Calling more than once will result in an exception.
     */
    void onComplete();
  }

  public interface Listener<T> {

    /**
     * Notifies the listener that the backstack will forward to a new Screen.
     *
     * @param newScreen The new Screen to be shown.
     * @param callback  Must be called to indicate completion.
     */
    void forward(@NonNull Screen<T> newScreen, @Nullable TransitionAnimator animator, @NonNull Callback callback);

    /**
     * Notifies the listener that the backstack will be moved back to given Screen.
     *
     * @param newScreen The new screen to be shown.
     * @param callback  Must be called to indicate completion.
     */
    void backward(@NonNull Screen<T> newScreen, @Nullable TransitionAnimator animator, @NonNull Callback callback);

    /**
     * Notifies the listener that the backstack will be replaced, with given Screen on top.
     *
     * @param newScreen The new screen to be shown.
     * @param callback  Must be called to indicate completion.
     */
    void replace(@NonNull Screen<T> newScreen, @Nullable TransitionAnimator animator, @NonNull Callback callback);
  }

  public interface ActivityResultListener {

    void onActivityResult(int resultCode, @Nullable Intent data);
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

    protected void forward(@NonNull final Backstack nextBackstack) {
      checkState(mListener != null, "Listener is null. Be sure to call setListener(Listener).");

      mNextBackstack = nextBackstack;
      mListener.forward(nextBackstack.current().screen, nextBackstack.current().animator, this);
    }

    protected void backward(@NonNull final Backstack nextBackstack, @Nullable final TransitionAnimator animator) {
      checkState(mListener != null, "Listener is null. Be sure to call setListener(Listener).");

      mNextBackstack = nextBackstack;
      mListener.backward(nextBackstack.current().screen, animator, this);
    }

    protected void replace(@NonNull final Backstack nextBackstack) {
      checkState(mListener != null, "Listener is null. Be sure to call setListener(Listener).");

      mNextBackstack = nextBackstack;
      mListener.replace(nextBackstack.current().screen, nextBackstack.current().animator, this);
    }

    protected abstract void execute();

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
        Backstack.Entry<?> entry = checkNotNull(builder.pop(), "Popped entry is null.");
        Backstack newBackstack = builder.build();
        backward(newBackstack, entry.animator);
      }
    }
  }

  private class ReplaceWithTransition extends Transition {

    @NonNull
    private final Screen<?> mScreen;

    @Nullable
    private final TransitionAnimator mAnimator;

    private ReplaceWithTransition(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
      mScreen = screen;
      mAnimator = animator;
    }

    @Override
    public void execute() {
      Backstack.Builder builder = mBackstack.buildUpon();
      builder.pop();
      builder.push(mScreen, mAnimator);
      Backstack newBackstack = builder.build();

      replace(newBackstack);
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
      forward(mNewBackstack);
    }
  }

  private class PopToTransition extends Transition {

    @NonNull
    private final Screen<?> mScreen;

    @Nullable
    private final TransitionAnimator mAnimator;

    private PopToTransition(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
      mScreen = screen;
      mAnimator = animator;
    }

    @Override
    public void execute() {
      if (mBackstack.current().screen.equals(mScreen)) {
        onComplete();
        return;
      }

      Backstack.Builder builder = mBackstack.buildUpon();
      int count = 0;
      // Take care to leave the original screen instance on the stack, if we find it.  This enables
      // some arguably bad behavior on the part of clients, but it's still probably the right thing
      // to do.
      Backstack.Entry<?> lastPoppedEntry = null;
      for (Iterator<Backstack.Entry<?>> it = mBackstack.reverseEntryIterator(); it.hasNext(); ) {
        Screen<?> screen = it.next().screen;

        if (screen.equals(mScreen)) {
          // Clear up to the target screen.
          for (int i = 0; i < mBackstack.size() - count; i++) {
            lastPoppedEntry = builder.pop();
          }
          break;
        } else {
          count++;
        }
      }

      Backstack newBackstack;
      if (lastPoppedEntry != null) {
        builder.push(lastPoppedEntry.screen, lastPoppedEntry.animator);
        newBackstack = builder.build();
        backward(newBackstack, mAnimator);
      } else {
        builder.push(mScreen, mAnimator);
        newBackstack = builder.build();
        forward(newBackstack);
      }
    }
  }

  private class ShowTransition extends Transition {

    @Override
    public void execute() {
      forward(mBackstack);
    }
  }

  private class StartWithTransition extends Transition {

    @NonNull
    private final Screen<?> mScreen;

    private StartWithTransition(@NonNull final Screen<?> screen) {
      mScreen = screen;
    }

    @Override
    public void execute() {
      Backstack newBackstack = mBackstack.buildUpon().push(mScreen).build();
      forward(newBackstack);
    }
  }

  private class GoToTransition extends Transition {

    @NonNull
    private final Screen<?> mScreen;

    @Nullable
    private final TransitionAnimator mAnimator;

    private GoToTransition(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
      mScreen = screen;
      mAnimator = animator;
    }

    @Override
    public void execute() {
      Backstack newBackstack = mBackstack.buildUpon().push(mScreen, mAnimator).build();
      forward(newBackstack);
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
      backward(mNewBackstack, null);
    }
  }

  private class ReplaceTransition extends Transition {

    @NonNull
    private final Backstack mNewBackstack;

    private ReplaceTransition(@NonNull final Backstack newBackstack) {
      mNewBackstack = newBackstack;
    }

    @Override
    public void execute() {
      replace(mNewBackstack);
    }
  }
}
