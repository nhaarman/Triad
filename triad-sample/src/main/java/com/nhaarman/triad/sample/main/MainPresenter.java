package com.nhaarman.triad.sample.main;

import com.nhaarman.triad.presenter.ScreenPresenter;
import com.nhaarman.triad.sample.main.maindialog.MainDialogScreen;
import flow.Flow;
import org.jetbrains.annotations.NotNull;

class MainPresenter extends ScreenPresenter<MainPresenter, MainContainer> {

  @NotNull
  private final MainViewModel mViewModel;

  @NotNull
  private final Flow mFlow;

  private int mCounter;
  private int mSecondCounter;

  MainPresenter(@NotNull final Flow flow) {
    mFlow = flow;

    mCounter = 0;
    mSecondCounter = 0;
    mViewModel = new MainViewModel();
  }

  @Override
  protected void onControlGained(@NotNull final MainContainer container) {
    container.setViewModel(mViewModel);
    updateViewModel();
  }

  public void onIncrementButtonClicked() {
    mCounter++;
    updateViewModel();

    if (mCounter % 10 == 0) {
      mFlow.goTo(new MainDialogScreen());
    }
  }

  public void onSecondIncrementButtonClicked() {
    mSecondCounter++;
    updateViewModel();
  }

  public void onNewScreenButtonClicked() {
    mFlow.goTo(new MainScreen());
  }

  private void updateViewModel() {
    mViewModel.setCounterText(String.valueOf(mCounter));
    mViewModel.setSecondCounterText(String.valueOf(mSecondCounter));
    mViewModel.notifyObservers();
  }
}
