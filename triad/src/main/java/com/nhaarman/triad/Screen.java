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
 * A class that creates the {@link ScreenContainer} and {@link ScreenPresenter} for a screen in the application.
 * <p/>
 * Since the creation of a {@link Presenter} may need additional dependencies, a {@code main component} is supplied
 * when requesting the {@link Presenter} instance. This {@code main component} should contain all dependencies necessary
 * for the {@link Presenter}, and is to be supplied by the implementer.
 *
 * @param <P> The specialized {@link ScreenPresenter} type.
 * @param <C> The specialized {@link ScreenContainer} type.
 * @param <M> The type of the {@code main component}.
 */
public abstract class Screen<P extends ScreenPresenter<P, C>, C extends ScreenContainer<P, C>, M> implements TransitionAnimator {

  /**
   * The {@link P} that is tied to this {@link Screen} instance.
   */
  @Nullable
  private P mPresenter;

  /**
   * Returns the layout resource id for this {@code Screen}.
   * <p/>
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
   * @param component The {@code main component} that can be used to create the {@link P}.
   *
   * @return The created {@link P}.
   */
  @NonNull
  protected abstract P createPresenter(@NonNull M component);

  /**
   * Inflates the layout resource id returned by {@link #getLayoutResId()}, and returns the {@link C} instance.
   * Does not attach the layout to {@code parent}.
   *
   * @param parent The parent {@link ViewGroup} the created {@link View} will be attached to.
   *
   * @return The created {@link C}.
   */
  @NonNull
  final ViewGroup createView(@NonNull final ViewGroup parent) {
    Context context = parent.getContext();

    if (getThemeResId() != -1) {
      context = new ContextThemeWrapper(context, getThemeResId());
    }

    return (ViewGroup) LayoutInflater.from(context).inflate(getLayoutResId(), parent, false);
  }

  /**
   * Returns the {@link P} that is tied to this {@code Screen} instance.
   * This instance is lazily instantiated.
   *
   * @param component The {@code main component} to retrieve dependencies from.
   * @param triad The Triad instance of the application.
   *
   * @return The {@link P}.
   */
  @NonNull
  private P getPresenter(@NonNull final M component, @NonNull final Triad triad) {
    if (mPresenter == null) {
      mPresenter = createPresenter(component);
      mPresenter.setTriad(triad);
    }

    return mPresenter;
  }

  @Override
  public boolean animateTransition(@Nullable final View oldView, @NonNull final View newView, @NonNull final Triad.Direction direction, @NonNull final Triad.Callback callback) {
    return false;
  }

  void acquirePresenter(@NonNull final M component, @NonNull final Triad triad, @NonNull final ViewGroup container) {
    ((Container<P, C>) container).setPresenter(getPresenter(component, triad));
  }

  void releaseContainer(@NonNull final M component, @NonNull final Triad triad) {
    getPresenter(component, triad).releaseContainer();
  }

  boolean onBackPressed(@NonNull final M component, @NonNull final Triad triad) {
    return getPresenter(component, triad).onBackPressed();
  }
}
