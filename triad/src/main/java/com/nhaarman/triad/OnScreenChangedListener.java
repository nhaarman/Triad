package com.nhaarman.triad;

import org.jetbrains.annotations.NotNull;

/**
 * An interface to provide a callback when a new screen is presented.
 *
 * @param <M> The MainComponent in the application.
 */
public interface OnScreenChangedListener<M> {

  /**
   * Callback method which is called when a new screen is presented.
   *
   * @param screen The newly added screen.
   */
  void onScreenChanged(@NotNull Screen<?, ?, M> screen);
}
