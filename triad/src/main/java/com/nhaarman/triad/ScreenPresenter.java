package com.nhaarman.triad;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  @NotNull
  protected Triad getTriad() {
    if (mTriad == null) {
      throw new IllegalStateException("Triad is null. Make sure setTriad(Triad) has been called with a valid instance.");
    }
    return mTriad;
  }

  /**
   * Sets the {@link Triad} instance to be used to navigate between {@link Screen}s.
   */
  public final void setTriad(@NotNull final Triad triad) {
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
