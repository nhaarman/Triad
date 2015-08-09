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

import static com.nhaarman.triad.Preconditions.checkState;

/**
 * The Presenter class.
 *
 * @param <ActivityComponent> The activity component.
 * @param <P>                 The specialized type of the {@link Presenter}.
 * @param <C>                 The specialized type of the {@link Container}.
 */
public class Presenter<
    ActivityComponent,
    P extends Presenter<ActivityComponent, P, C>,
    C extends Container<ActivityComponent, P, C>
    > {

  /**
   * The {@link C} this {@link Presenter} controls.
   */
  @Nullable
  C mContainer;

  @Nullable
  private ActivityComponent mActivityComponent;

  @Nullable
  private Triad mTriad;

  /**
   * Sets the {@link C} this {@code Presenter} controls, and calls {@link #onControlGained(Container, Object)} )}
   * to notify implementers of this class that the {@link C} is available.
   *
   * @param container The {@link C} to gain control over.
   */
  @MainThread
  public void acquire(@NonNull final C container, @NonNull final ActivityComponent activityComponent) {
    if (container.equals(mContainer)) {
      return;
    }

    if (mContainer != null) {
      onControlLost();
    }

    mContainer = container;
    mActivityComponent = activityComponent;
    onControlGained(container, activityComponent);
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
    mActivityComponent = null;
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
  protected void onControlGained(@NonNull final C container,
                                 @NonNull final ActivityComponent activityComponent) {
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
   * Returns the {@link ActivityComponent}.
   */
  @NonNull
  public Optional<ActivityComponent> getActivityComponent() {
    return Optional.of(mActivityComponent);
  }

  /**
   * Returns the {@link Triad} instance to be used to navigate between {@link Screen}s.
   */
  @NonNull
  protected Triad getTriad() {
    checkState(mTriad != null, "Triad is null. Make sure setTriad(Triad) has been called with a valid instance.");

    return mTriad;
  }

  /**
   * Sets the {@link Triad} instance to be used to navigate between {@link Screen}s.
   */
  public final void setTriad(@NonNull final Triad triad) {
    mTriad = triad;
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
