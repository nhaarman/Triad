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

import android.content.res.Resources;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
  C mContainer;

  /**
   * Sets the {@link C} this {@code Presenter} controls, and calls {@link #onControlGained(Container)}
   * to notify implementers of this class that the {@link C} is available.
   *
   * @param container The {@link C} to gain control over.
   */
  @MainThread
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
  @MainThread
  public void releaseContainer() {
    if (mContainer == null) {
      return;
    }

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
  @MainThread
  protected void onControlGained(@NonNull final C container) {
  }

  /**
   * Called when this {@code Presenter} no longer controls the {@link C}.
   * From this point on, {@link #getContainer()} will return {@link null}.
   */
  @MainThread
  protected void onControlLost() {
  }

  /**
   * Returns the {@link C} instance this {@code Presenter} controls.
   */
  @NonNull
  public Optional<C> getContainer() {
    return Optional.of(mContainer);
  }

  /**
   * Return a Resources instance for your application's package.
   */
  @NonNull
  public Optional<Resources> getResources() {
    if (mContainer == null) {
      return Optional.of(null);
    } else {
      return Optional.of(mContainer.getContext().getResources());
    }
  }
}
