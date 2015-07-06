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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.nhaarman.triad.Preconditions.checkState;

/**
 * A {@link Presenter} class that is used in combination with a {@link Screen} and a {@link ScreenContainer}.
 *
 * @param <P> The specialized {@link ScreenPresenter} type.
 * @param <C> The specialized {@link ScreenContainer} type.
 */
public abstract class ScreenPresenter<P extends Presenter<P, C>, C extends Container<P, C>> extends Presenter<P, C> {

  @Nullable
  private Triad mTriad;

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
