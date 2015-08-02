/*
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

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import static com.nhaarman.triad.Preconditions.checkState;

/**
 * This class represents a delegate which can be used to use Triad in any
 * {@link Activity}.
 * <p/>
 * When using the {@code TriadDelegate}, you must proxy the following Activity
 * lifecycle methods to it:
 * <ul>
 * <li>{@link #onCreate(Object)}</li>
 * <li>{@link #onStart()}</li>
 * <li>{@link #onPostCreate()}</li>
 * <li>{@link #onStop()}</li>
 * <li>{@link #onBackPressed()}</li>
 * </ul>
 *
 * @param <M> The {@code main component} to use for {@link Presenter} creation.
 */
public class TriadDelegate<M> {

  /**
   * The {@link Activity} instance this {@code TriadDelegate} is bound to.
   */
  @NonNull
  private final Activity mActivity;

  @Nullable
  private M mMainComponent;

  /**
   * The {@link Triad} instance that is used to navigate between {@link Screen}s.
   */
  @Nullable
  private Triad mTriad;

  @SuppressWarnings("rawtypes")
  @Nullable
  private Screen mCurrentScreen;

  @Nullable
  private ViewGroup mCurrentView;

  /**
   * An optional {@link OnScreenChangedListener} that is notified of screen changes.
   */
  @Nullable
  private OnScreenChangedListener<M> mOnScreenChangedListener;

  private TriadView mTriadView;

  public TriadDelegate(@NonNull final Activity activity) {
    mActivity = activity;
  }

  public void onCreate(@NonNull final M mainComponent) {
    checkState(mActivity.getApplication() instanceof TriadProvider, "Make sure your Application class implements TriadProvider.");

    mMainComponent = mainComponent;

    mActivity.setContentView(R.layout.view_triad);
    mTriadView = (TriadView) mActivity.findViewById(R.id.view_triad);

    mTriad = ((TriadProvider) mActivity.getApplication()).getTriad();
    mTriad.setActivity(mActivity);
    mTriad.setListener(new MyFlowListener());

    if (mTriad.getBackstack().size() > 0) {
      Screen<?, ?, ?> current = mTriad.getBackstack().current();
      assert current != null;
      mTriad.popTo(current);
    }
  }

  public void onStart() {
    checkState(mMainComponent != null, "MainComponent is null. Make sure to call TriadDelegate.onCreate(M).");
    checkState(mTriad != null, "Triad is null. Make sure to call TriadDelegate.onCreate(M).");

    if (mCurrentScreen == null || mCurrentView == null) {
      return;
    }

    mCurrentScreen.acquirePresenter(mMainComponent, mTriad,  mCurrentView);
  }

  public void onPostCreate() {

  }

  public void onStop() {
    checkState(mMainComponent != null, "TriadPresenter is null. Make sure to call TriadDelegate.onCreate(M).");
    checkState(mTriad != null, "Triad is null. Make sure to call TriadDelegate.onCreate(M).");

    if (mCurrentScreen == null) {
      return;
    }

    mCurrentScreen.releaseContainer(mMainComponent, mTriad);
  }

  public boolean onBackPressed() {
    checkState(mMainComponent != null, "TriadPresenter is null. Make sure to call TriadDelegate.onCreate(M).");
    checkState(mTriad != null, "Triad is null. Make sure to call TriadDelegate.onCreate(M).");

    return mCurrentScreen != null && mCurrentScreen.onBackPressed(mMainComponent, mTriad) || mTriad.goBack();
  }

  public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    checkState(mTriad != null, "Triad is null. Make sure to call TriadDelegate.onCreate(M)");

    mTriad.onActivityResult(requestCode, resultCode, data);
  }

  /**
   * Returns the {@link Triad} instance to be used to navigate between {@link Screen}s.
   */
  @NonNull
  public Triad getTriad() {
    checkState(mTriad != null, "Triad is null. Make sure to call TriadDelegate.onCreate(M).");

    return mTriad;
  }

  /**
   * Sets an {@link OnScreenChangedListener} to be notified of screen changes.
   */
  public void setOnScreenChangedListener(@Nullable final OnScreenChangedListener<M> onScreenChangedListener) {
    mOnScreenChangedListener = onScreenChangedListener;
  }

  private void onScreenChanged(@NonNull final Screen<?, ?, M> screen) {
    if (mOnScreenChangedListener != null) {
      mOnScreenChangedListener.onScreenChanged(screen);
    }
  }

  private class MyFlowListener implements Triad.Listener {

    @Override
    public void go(final Backstack nextBackstack, final Triad.Direction direction, final Triad.Callback callback) {
      checkState(nextBackstack.size() > 0, "Empty backstack");
      //noinspection rawtypes
      Screen screen = nextBackstack.current();
      assert screen != null;
      showScreen(screen, direction, callback);

      onScreenChanged(screen);
    }

    /**
     * Performs the proper transitions to show given {@link Screen}.
     *
     * @param screen The {@link Screen} to show.
     */
    private void showScreen(@NonNull final Screen screen,
                            @NonNull final Triad.Direction direction,
                            @NonNull final Triad.Callback callback) {
      checkState(mMainComponent != null, "TriadPresenter is null. Make sure to call TriadDelegate.onCreate(M).");
      checkState(mTriad != null, "Triad is null. Make sure to call TriadDelegate.onCreate(M).");

      mCurrentScreen = screen;

      ViewGroup container = screen.createView(mTriadView);
      screen.acquirePresenter(mMainComponent, mTriad, container);

      mTriadView.transition(mCurrentView, container, direction, callback, screen);

      mCurrentView = container;
    }
  }
}
