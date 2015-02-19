package com.nhaarman.gable.container;

import com.nhaarman.gable.presenter.Presenter;
import org.jetbrains.annotations.NotNull;

/**
 * The Container interface.
 *
 * @param <P> The specialized type of the {@link Presenter} for this {@link Container}.
 * @param <C> The specialized type of the {@link Container}.
 */
public interface Container<P extends Presenter<P, C>, C extends Container<P, C>> {

  /**
   * Sets the {@link Presenter} that should control this {@code Container}.
   * Implementers must ensure that {@link Presenter#acquire(Container)} and {@link Presenter#releaseContainer()}
   * are called at proper times.
   *
   * @param presenter The {@link Presenter} instance.
   */
  void setPresenter(@NotNull P presenter);
}
