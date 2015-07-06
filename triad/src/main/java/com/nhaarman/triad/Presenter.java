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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import static com.nhaarman.triad.Preconditions.checkState;

/**
 * The Presenter class.
 *
 * @param <P> The specialized type of the {@link Presenter}.
 * @param <C> The specialized type of the {@link Container}.
 */
public class Presenter<P extends Presenter<P, C>, C extends Container<P, C>> {

  /**
   * The {@link C} this {@link Presenter} controls.
   */
  @Nullable
  private C mContainer;

  /**
   * Sets the {@link C} this {@code Presenter} controls, and calls {@link #onControlGained(Container)}
   * to notify implementers of this class that the {@link C} is available.
   *
   * @param container The {@link C} to gain control over.
   */
  public void acquire(@NonNull final C container) {
    if (container.equals(mContainer)) {
      return;
    }

    if (mContainer != null) {
      onControlLost();
    }

    mContainer = container;
    onControlGained(container);
  }

  /**
   * Releases the {@link C} this {@code Presenter} controls, and calls {@link #onControlLost()}
   * to notify implementers of this class that the {@link C} is no longer available.
   */
  public void releaseContainer() {
    mContainer = null;
    onControlLost();
  }

  /**
   * Called when the {@link Container} for this {@code Presenter} is attached to the window
   * and ready to display the state.
   * From this point on, {@link #getContainer()} will return the {@link C} instance, until {@link #onControlLost()} is called.
   *
   * @param container The {@link C} to gain control over.
   */
  protected void onControlGained(@NonNull final C container) {
  }

  /**
   * Called when this {@code Presenter} no longer controls the {@link C}.
   * From this point on, {@link #getContainer()} will return {@link null}.
   */
  protected void onControlLost() {
  }

  /**
   * Returns the {@link C} instance this {@code Presenter} controls.
   */
  @NonNull
  protected Optional<C> getContainer() {
    return Optional.of(mContainer);
  }

  /**
   * Return a localized formatted string from the application's package's
   * default string table, substituting the format arguments as defined in
   * {@link java.util.Formatter} and {@link java.lang.String#format}.
   *
   * @param resId Resource id for the format string
   * @param formatArgs The format arguments that will be used for substitution.
   *
   * @see {@link Context#getString(int, Object...)}.
   */
  protected final String getString(@StringRes final int resId, @Nullable final Object... formatArgs) {
    checkState(mContainer != null, "Presenter has no control over any Container.");

    return mContainer.getContext().getString(resId, formatArgs);
  }
}
