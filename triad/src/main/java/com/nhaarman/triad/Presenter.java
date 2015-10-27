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
import android.support.annotation.VisibleForTesting;

/**
 * The Presenter class.
 *
 * @param <ActivityComponent> The activity component.
 * @param <C>                 The specialized type of the {@link Container}.
 */
public class Presenter<C extends Container, ActivityComponent> {

  /**
   * The {@link Container} this {@link Presenter} controls.
   */
  @NonNull
  private Optional<C> mContainer = Optional.empty();

  @NonNull
  private Optional<Resources> mResources = Optional.empty();

  @Nullable
  private ActivityComponent mActivityComponent;

  /**
   * Sets the {@link C} this {@code Presenter} controls, and calls {@link #onControlGained(Container, Object)} )}
   * to notify implementers of this class that the {@link C} is available.
   *
   * @param container The {@link C} to gain control over.
   */
  @MainThread
  public void acquire(@NonNull final C container, @NonNull final ActivityComponent activityComponent) {
    if (mContainer.isPresent() && container.equals(mContainer.get())) {
      return;
    }

    if (mContainer.isPresent()) {
      onControlLost();
    }

    mContainer = Optional.of(container);
    mResources = getResources(container);
    mActivityComponent = activityComponent;
    onControlGained(container, activityComponent);
  }

  @VisibleForTesting
  public void setContainer(@NonNull final C container) {
    mContainer = Optional.of(container);
    mResources = getResources(container);
  }

  /* Perform null checks to avoid unit tests from failing. */
  private Optional<Resources> getResources(@NonNull final C container) {
    //noinspection ConstantConditions
    if (container.getContext() != null && container.getContext().getResources() != null) {
      return Optional.of(container.getContext().getResources());
    }

    return Optional.empty();
  }

  /**
   * Releases the {@link C} this {@code Presenter} controls, and calls {@link #onControlLost()}
   * to notify implementers of this class that the {@link C} is no longer available.
   */
  @MainThread
  public void releaseContainer() {
    if (!mContainer.isPresent()) {
      return;
    }

    mContainer = Optional.empty();
    mResources = Optional.empty();
    mActivityComponent = null;
    onControlLost();
  }

  /**
   * Called when the {@link Container} for this {@code Presenter} is attached to the window
   * and ready to display the state.
   * From this point on, {@link #container()} will return the {@link C} instance, until {@link #onControlLost()} is called.
   *
   * @param container The {@link C} to gain control over.
   */
  @MainThread
  protected void onControlGained(@NonNull final C container,
                                 @NonNull final ActivityComponent activityComponent) {
  }

  /**
   * Called when this {@code Presenter} no longer controls the {@link C}.
   * From this point on, {@link #container()} will return {@link null}.
   */
  @MainThread
  protected void onControlLost() {
  }

  /**
   * Returns the {@link C} instance this {@code Presenter} controls.
   */
  @NonNull
  public Optional<C> container() {
    return mContainer;
  }

  /**
   * Returns the {@link ActivityComponent}.
   */
  @NonNull
  public Optional<ActivityComponent> activityComponent() {
    return Optional.of(mActivityComponent);
  }

  /**
   * Return a Resources instance for your application's package.
   */
  @NonNull
  public Optional<Resources> getResources() {
    return mResources;
  }
}
