package com.nhaarman.triad.tests.seconddialog;

import com.nhaarman.triad.screen.Screen;
import com.nhaarman.triad.tests.R;
import com.nhaarman.triad.tests.TestComponent;
import org.jetbrains.annotations.NotNull;

public class SecondDialogScreen extends Screen<SecondDialogPresenter, SecondDialogContainer, TestComponent> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_dialog_second;
  }

  @NotNull
  @Override
  protected SecondDialogPresenter createPresenter(@NotNull final TestComponent testComponent) {
    return new SecondDialogPresenter();
  }

  @Override
  public boolean isDialog() {
    return true;
  }
}
