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

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.nhaarman.triad.Preconditions.checkState;

/**
 * A {@link Presenter} class that is used in combination with a {@link Screen} and a {@link ScreenContainer}.
 *
 * @param <P> The specialized {@link ScreenPresenter} type.
 * @param <C> The specialized {@link ScreenContainer} type.
 * @param <ActivityComponent> The activity component.
 */
public class ScreenPresenter<ActivityComponent, P extends Presenter<P, C>, C extends Container<P, C>> extends Presenter<P, C> {

  @Nullable
  private Triad mTriad;

  @Nullable
  private ActivityComponent mActivityComponent;

  @Override
  @Deprecated
  public void acquire(@NonNull final C container) {
    throw new IllegalStateException("Use acquire(C, ActivityComponent) instead.");
  }

  /**
   * Sets the {@link C} this {@code Presenter} controls, and calls {@link #onControlGained(Container)}
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

  @Override
  @Deprecated
  protected final void onControlGained(@NonNull final C container) {
    throw new IllegalStateException("Use onControlGained(C, ActivityComponent) instead.");
  }

  /**
   * Called when the {@link Container} for this {@code Presenter} is attached to the window
   * and ready to display the state.
   * From this point on, {@link #getContainer()} will return the {@link C} instance, until {@link #onControlLost()} is called.
   *
   * @param container The {@link C} to gain control over.
   * @param activityComponent The {@link ActivityComponent}.
   */
  @MainThread
  protected void onControlGained(@NonNull final C container, @NonNull final ActivityComponent activityComponent) {
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
   * Callback for when the back button has been pressed.
   *
   * @return true if the event has been handled, false otherwise.
   */
  public boolean onBackPressed() {
    return false;
  }
}
