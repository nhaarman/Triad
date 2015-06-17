package com.nhaarman.triad;

import android.view.View;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Container} which hosts all {@link View}s belonging to {@link Screen}s in the application.
 *
 * @param <M> The main module in the application. See {@link TriadPresenter}.
 */
interface TriadContainer<M> extends Container<TriadPresenter<M>, TriadContainer<M>> {

  /**
   * Transitions from {@code oldView} to {@code newView}.
   * If both {@link View}s are @link null}, nothing happens.
   *
   * @param oldView The old {@link View} to display an exit animation for.
   * @param newView The new {@link View} to display an entering animation for.
   */
  void transition(@Nullable View oldView, @Nullable View newView);

}
