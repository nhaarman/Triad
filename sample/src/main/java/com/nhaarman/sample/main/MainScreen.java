package com.nhaarman.sample.main;

import com.nhaarman.gable.screen.Screen;
import com.nhaarman.sample.MainComponent;
import com.nhaarman.sample.R;
import org.jetbrains.annotations.NotNull;

public class MainScreen extends Screen<MainPresenter, MainContainer, MainComponent> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_main;
  }

  @Override
  @NotNull
  protected MainPresenter createPresenter(@NotNull final MainComponent mainComponent) {
    return new MainPresenter();
  }
}
