/*
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
import android.view.ViewGroup;

import static com.nhaarman.triad.Preconditions.checkNotNull;
import static com.nhaarman.triad.Preconditions.checkState;

/**
 * This class represents a delegate which can be used to use Triad in any
 * {@link Activity}.
 * <p/>
 * When using the {@code TriadDelegate}, you must proxy the following Activity
 * lifecycle methods to it:
 * <ul>
 * <li>{@link #onCreate(Object)}</li>
 * <li>{@link #onBackPressed()}</li>
 * <li>{@link #onActivityResult(int, int, Intent)}</li>
 * </ul>
 *
 * @param <ApplicationComponent> The {@code ApplicationComponent} to use for {@code Presenter} creation.
 * @param <ActivityComponent>    The {@code ActivityComponent} to supply to {@code Presenters}.
 */
public class TriadDelegate<ApplicationComponent, ActivityComponent> {

  /**
   * The {@link Activity} instance this {@code TriadDelegate} is bound to.
   */
  @NonNull
  private final Activity mActivity;

  @Nullable
  private ApplicationComponent mApplicationComponent;

  @Nullable
  private ActivityComponent mActivityComponent;

  /**
   * The {@link Triad} instance that is used to navigate between {@link Screen}s.
   */
  @Nullable
  private Triad mTriad;

  @Nullable
  private Screen<ApplicationComponent> mCurrentScreen;

  @Nullable
  private ViewGroup mCurrentView;

  /**
   * An optional {@link OnScreenChangedListener} that is notified of screen changes.
   */
  @Nullable
  private OnScreenChangedListener<ApplicationComponent, ActivityComponent> mOnScreenChangedListener;

  private TriadView mTriadView;

  private TriadDelegate(@NonNull final Activity activity) {
    mActivity = activity;
  }

  @NonNull
  public Screen<ApplicationComponent> getCurrentScreen() {
    return checkNotNull(mCurrentScreen, "Current screen is null.");
  }

  public void onCreate(@NonNull final ActivityComponent activityComponent) {
    checkState(mActivity.getApplication() instanceof TriadProvider, "Make sure your Application class implements TriadProvider.");
    checkState(mActivity.getApplication() instanceof ApplicationComponentProvider, "Make sure your Application class implements ApplicationComponentProvider.");

    mApplicationComponent = ((ApplicationComponentProvider<ApplicationComponent>) mActivity.getApplication()).getApplicationComponent();
    mActivityComponent = activityComponent;

    mActivity.setContentView(R.layout.view_triad);
    mTriadView = (TriadView) mActivity.findViewById(R.id.view_triad);

    mTriad = ((TriadProvider) mActivity.getApplication()).getTriad();
    mTriad.setActivity(mActivity);
    mTriad.setListener(new MyFlowListener());

    if (mTriad.getBackstack().size() > 0) {
      Screen<?> current = mTriad.getBackstack().current();
      assert current != null;
      mTriad.popTo(current);
    }
  }

  public boolean onBackPressed() {
    checkState(mTriad != null, "Triad is null. Make sure to call TriadDelegate.onCreate(ActivityComponent).");

    return mCurrentScreen != null && mCurrentScreen.onBackPressed() || mTriad.goBack();
  }

  public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    checkState(mTriad != null, "Triad is null. Make sure to call TriadDelegate.onCreate(ActivityComponent)");

    mTriad.onActivityResult(requestCode, resultCode, data);
  }

  /**
   * Returns the {@link Triad} instance to be used to navigate between {@link Screen}s.
   */
  @NonNull
  public Triad getTriad() {
    checkState(mTriad != null, "Triad is null. Make sure to call TriadDelegate.onCreate(ActivityComponent).");

    return mTriad;
  }

  /**
   * Sets an {@link OnScreenChangedListener} to be notified of screen changes.
   */
  public void setOnScreenChangedListener(@Nullable final OnScreenChangedListener<ApplicationComponent, ActivityComponent> onScreenChangedListener) {
    mOnScreenChangedListener = onScreenChangedListener;
  }

  private void onScreenChanged(@NonNull final Screen<ApplicationComponent> screen) {
    if (mOnScreenChangedListener != null) {
      mOnScreenChangedListener.onScreenChanged(screen);
    }
  }

  @NonNull
  public static <T, V> TriadDelegate<T, V> createFor(@NonNull final Activity activity) {
    return new TriadDelegate(activity);
  }

  private class MyFlowListener implements Triad.Listener {

    @Override
    public void go(final Backstack nextBackstack, final Triad.Direction direction, final Triad.Callback callback) {
      checkState(nextBackstack.size() > 0, "Empty backstack");
      Screen<ApplicationComponent> screen = (Screen<ApplicationComponent>) nextBackstack.current();
      assert screen != null;
      showScreen(screen, direction, callback);

      onScreenChanged(screen);
    }

    /**
     * Performs the proper transitions to show given {@link Screen}.
     *
     * @param screen The {@link Screen} to show.
     */
    private void showScreen(@NonNull final Screen<ApplicationComponent> screen,
                            @NonNull final Triad.Direction direction,
                            @NonNull final Triad.Callback callback) {
      checkState(mApplicationComponent != null, "ApplicationComponent is null. Make sure to call TriadDelegate.onCreate(ActivityComponent).");
      checkState(mActivityComponent != null, "ActivityComponent is null. Make sure to call TriadDelegate.onCreate(ActivityComponent).");
      checkState(mTriad != null, "Triad is null. Make sure to call TriadDelegate.onCreate(ActivityComponent).");

      mCurrentScreen = screen;

      screen.setApplicationComponent(mApplicationComponent);
      ViewGroup container = screen.createView(mTriadView);
      mTriadView.transition(mCurrentView, container, direction, callback, screen);

      mCurrentView = container;
    }
  }
}
