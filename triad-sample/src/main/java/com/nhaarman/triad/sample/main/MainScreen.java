package com.nhaarman.triad.sample.main;

import com.nhaarman.triad.screen.Screen;
import com.nhaarman.triad.sample.MainComponent;
import com.nhaarman.triad.sample.R;
import org.jetbrains.annotations.NotNull;

/**
 * A screen with one simple counter in portrait mode, and two counters in landscape mode.
 */
public class MainScreen extends Screen<MainPresenter, MainContainer, MainComponent> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_main;
  }

  @Override
  @NotNull
  protected MainPresenter createPresenter(@NotNull final MainComponent mainComponent) {
    return new MainPresenter(mainComponent.flow());
  }
}
