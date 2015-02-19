package com.nhaarman.sample.main;

import com.nhaarman.gable.presenter.ScreenPresenter;
import org.jetbrains.annotations.NotNull;

class MainPresenter extends ScreenPresenter<MainPresenter, MainContainer> {

  @NotNull
  private final MainViewModel mViewModel;

  private int mCounter;
  private int mSecondCounter;

  MainPresenter() {
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
  }

  public void onSecondIncrementButtonClicked() {
    mSecondCounter++;
    updateViewModel();
  }

  private void updateViewModel() {
    mViewModel.setCounterText(String.valueOf(mCounter));
    mViewModel.setSecondCounterText(String.valueOf(mSecondCounter));
    mViewModel.notifyObservers();
  }
}
