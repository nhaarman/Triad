package com.nhaarman.triad;

import flow.Flow;
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
  private Flow mFlow;

  /**
   * Returns the {@link Flow} instance to be used to navigate between {@link Screen}s.
   */
  @NotNull
  protected Flow getFlow() {
    if (mFlow == null) {
      throw new IllegalStateException("Flow is null. Make sure setFlow(Flow) has been called with a valid instance.");
    }
    return mFlow;
  }

  /**
   * Sets the {@link Flow} instance to be used to navigate between {@link Screen}s.
   */
  public final void setFlow(@NotNull final Flow flow) {
    mFlow = flow;
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
