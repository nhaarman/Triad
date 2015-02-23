package com.nhaarman.triad.sample.main.maindialog;

import com.nhaarman.triad.presenter.ScreenPresenter;
import flow.Flow;
import org.jetbrains.annotations.NotNull;

class MainDialogPresenter extends ScreenPresenter<MainDialogPresenter, MainDialogContainer> {

  @NotNull
  private final Flow mFlow;

  MainDialogPresenter(@NotNull final Flow flow) {
    mFlow = flow;
  }

  public void onButtonClicked() {
    mFlow.goTo(new MainDialogScreen());
  }
}
