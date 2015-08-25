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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.ButterKnife;

/**
 * A delegate class that can be used for handling Container implementations.
 * Users of this class must proxy the following methods:
 *
 * <ul>
 * <li>{@link #getPresenter()}</li>
 * <li>{@link #setPresenterAndActivityComponent(Presenter, Object)}</li>
 * <li>{@link #getActivityComponent()}</li>
 * <li>{@link #onFinishInflate()}</li>
 * <li>{@link #onAttachedToWindow()}</li>
 * <li>{@link #onDetachedFromWindow()}</li>
 * </ul>
 */
public class ContainerDelegate<
    ActivityComponent,
    P extends Presenter<ActivityComponent, P, C>,
    C extends Container<ActivityComponent, P, C>
    > {

  @NonNull
  private final C mContainer;

  /**
   * The {@link P} that is tied to this instance.
   */
  @Nullable
  private P mPresenter;

  @Nullable
  private ActivityComponent mActivityComponent;

  /**
   * Whether we're attached to the window.
   */
  private boolean mAttachedToWindow;

  public ContainerDelegate(@NonNull final C container) {
    mContainer = container;
  }

  /**
   * Returns the {@link P} instance that is tied to this {@code RelativeLayoutContainer}.
   */
  @NonNull
  public final P getPresenter() {
    if (mPresenter == null) {
      throw new NullPointerException("Presenter has not been set for " + getClass().getCanonicalName());
    }

    return mPresenter;
  }

  /**
   * Sets the {@link P} that controls this {@code RelativeLayoutContainer}.
   *
   * @param presenter The {@link P} instance.
   */
  public final void setPresenterAndActivityComponent(@NonNull final P presenter, @NonNull final ActivityComponent activityComponent) {
    mPresenter = presenter;
    mActivityComponent = activityComponent;

    if (mAttachedToWindow) {
      acquire();
    }
  }

  @NonNull
  public final ActivityComponent getActivityComponent() {
    if (mActivityComponent == null) {
      throw new NullPointerException("ActivityComponent has not been set for " + getClass().getCanonicalName());
    }

    return mActivityComponent;
  }

  public final void onFinishInflate() {
    if (((View) mContainer).isInEditMode()) {
      return;
    }

    ButterKnife.bind((View) mContainer);
  }

  public final void onAttachedToWindow() {
    mAttachedToWindow = true;

    if (((View) mContainer).isInEditMode()) {
      return;
    }

    acquire();
  }

  private void acquire() {
    getPresenter().acquire(mContainer, getActivityComponent());
  }

  public final void onDetachedFromWindow() {
    mAttachedToWindow = false;

    getPresenter().releaseContainer();
  }
}
