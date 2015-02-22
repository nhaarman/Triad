package com.nhaarman.triad.tests.firstdialog;

import com.nhaarman.triad.screen.Screen;
import com.nhaarman.triad.tests.R;
import com.nhaarman.triad.tests.TestComponent;
import org.jetbrains.annotations.NotNull;

public class FirstDialogScreen extends Screen<FirstDialogPresenter, FirstDialogContainer, TestComponent> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_dialog_first;
  }

  @NotNull
  @Override
  protected FirstDialogPresenter createPresenter(@NotNull final TestComponent testComponent) {
    return new FirstDialogPresenter();
  }

  @Override
  public boolean isDialog() {
    return true;
  }
}
