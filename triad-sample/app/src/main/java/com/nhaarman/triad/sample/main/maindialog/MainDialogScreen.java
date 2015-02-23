package com.nhaarman.triad.sample.main.maindialog;

import com.nhaarman.triad.sample.MainComponent;
import com.nhaarman.triad.sample.R;
import com.nhaarman.triad.screen.Screen;
import org.jetbrains.annotations.NotNull;

public class MainDialogScreen extends Screen<MainDialogPresenter, MainDialogContainer, MainComponent> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_main_dialog;
  }

  @NotNull
  @Override
  protected MainDialogPresenter createPresenter(@NotNull final MainComponent mainComponent) {
    return new MainDialogPresenter(mainComponent.flow());
  }

  @Override
  public boolean isDialog() {
    return true;
  }
}
