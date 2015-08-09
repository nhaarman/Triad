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

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A class that creates the {@link Container} and {@link Presenter} for a screen in the application.
 * <p>
 * Since the creation of a {@link Presenter} may need additional dependencies, an {@code ApplicationComponent} is supplied
 * when requesting the {@link Presenter} instance.
 *
 * @param <ApplicationComponent> The type of the {@code ApplicationComponent}.
 * @param <ActivityComponent>    The type of the {@code ActivityComponent}.
 * @param <P>                    The specialized {@link Presenter} type.
 * @param <C>                    The specialized {@link Container} type.
 */
public abstract class Screen
    <
        ApplicationComponent,
        ActivityComponent,
        P extends Presenter<ActivityComponent, P, C>,
        C extends Container<ActivityComponent, P, C>
        > implements TransitionAnimator {

  /**
   * The {@link P} that is tied to this {@link Screen} instance.
   */
  @Nullable
  private P mPresenter;

  /**
   * Returns the layout resource id for this {@code Screen}.
   * <p>
   * The root of this resource should be an implementation of {@link C}.
   */
  @LayoutRes
  protected abstract int getLayoutResId();

  @StyleRes
  protected int getThemeResId() {
    return -1;
  }

  /**
   * Creates a {@link P} for this {@code Screen}.
   *
   * @param applicationComponent The {@code activity component} that can be used to create the {@link P}.
   *
   * @return The created {@link P}.
   */
  @NonNull
  protected abstract P createPresenter(@NonNull ApplicationComponent applicationComponent);

  /**
   * Inflates the layout resource id returned by {@link #getLayoutResId()}, and returns the {@link C} instance.
   * Does not attach the layout to {@code parent}.
   *
   * @param parent The parent {@link ViewGroup} the created {@link View} will be attached to.
   *
   * @return The created {@link C}.
   */
  @NonNull
  protected ViewGroup createView(@NonNull final ViewGroup parent, @NonNull final ActivityComponent activityComponent) {
    Context context = wrapContext(parent.getContext());

    if (getThemeResId() != -1) {
      context = new ContextThemeWrapper(context, getThemeResId());
    }

    return (ViewGroup) LayoutInflater.from(context).inflate(getLayoutResId(), parent, false);
  }

  @NonNull
  protected Context wrapContext(@NonNull final Context context) {
    return context;
  }

  /**
   * Returns the {@link P} that is tied to this {@code Screen} instance.
   * This instance is lazily instantiated.
   *
   * @param applicationComponent The {@code activity component} to retrieve dependencies from.
   * @param triad                The Triad instance of the application.
   *
   * @return The {@link P}.
   */
  @NonNull
  private P getPresenter(@NonNull final ApplicationComponent applicationComponent, @NonNull final Triad triad) {
    if (mPresenter == null) {
      mPresenter = createPresenter(applicationComponent);
      mPresenter.setTriad(triad);
    }

    return mPresenter;
  }

  @Override
  public boolean animateTransition(@Nullable final View oldView,
                                   @NonNull final View newView,
                                   @NonNull final Triad.Direction direction,
                                   @NonNull final Triad.Callback callback) {
    return false;
  }

  void acquirePresenter(@NonNull final ApplicationComponent applicationComponent,
                        @NonNull final ActivityComponent activityComponent,
                        @NonNull final Triad triad,
                        @NonNull final ViewGroup container) {
    ((Container<ActivityComponent, P, C>) container).setPresenterAndActivityComponent(getPresenter(applicationComponent, triad), activityComponent);
  }

  void releaseContainer(@NonNull final ApplicationComponent applicationComponent, @NonNull final Triad triad) {
    getPresenter(applicationComponent, triad).releaseContainer();
  }

  boolean onBackPressed(@NonNull final ApplicationComponent applicationComponent, @NonNull final Triad triad) {
    return false;
  }
}
