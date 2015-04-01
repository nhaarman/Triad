package com.nhaarman.triad.presenter;

import com.nhaarman.triad.container.Container;
import com.nhaarman.triad.container.ScreenContainer;
import com.nhaarman.triad.screen.Screen;
import flow.Flow;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Presenter} class that is used in combination with a {@link Screen} and a {@link ScreenContainer}.
 *
 * @param <P> The specialized {@link ScreenPresenter} type.
 * @param <C> The specialized {@link ScreenContainer} type.
 */
public abstract class ScreenPresenter<P extends Presenter<P, C>, C extends Container<P, C>> extends Presenter<P, C> {

  private Flow mFlow;

  protected Flow getFlow() {
    return mFlow;
  }

  public final void setFlow(@NotNull final Flow flow) {
    mFlow = flow;
  }

  /**
   * Called when the back button has been pressed.
   *
   * @return true if the event has been handled, false otherwise.
   */
  public boolean onBackPressed() {
    return false;
  }
}
