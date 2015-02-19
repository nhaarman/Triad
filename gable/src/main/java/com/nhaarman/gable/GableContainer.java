package com.nhaarman.gable;

import android.view.View;
import com.nhaarman.gable.container.Container;
import com.nhaarman.gable.screen.Screen;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Container} which hosts all {@link View}s belonging to {@link Screen}s in the application.
 *
 * @param <M> The main module in the application. See {@link GablePresenter}.
 */
interface GableContainer<M> extends Container<GablePresenter<M>, GableContainer<M>> {

  /**
   * Transitions from {@code oldView} to {@code newView}.
   * If both {@link View}s are @link null}, nothing happens.
   *
   * @param oldView The old {@link View} to display an exit animation for.
   * @param newView The new {@link View} to display an entering animation for.
   */
  void transition(@Nullable View oldView, @Nullable View newView);
}
