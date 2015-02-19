package com.nhaarman.sample.main;

import com.nhaarman.gable.presenter.ViewModelScreenPresenter;
import org.jetbrains.annotations.NotNull;

class MainPresenter extends ViewModelScreenPresenter<MainPresenter, MainContainer, MainViewModel> {

  private int mCounter;
  private int mSecondCounter;

  MainPresenter() {
    mCounter = 0;
    mSecondCounter = 0;
  }

  @NotNull
  @Override
  protected MainViewModel createViewModel() {
    MainViewModel mainViewModel = new MainViewModel();
    updateViewModel(mainViewModel);
    return mainViewModel;
  }

  public void onIncrementButtonClicked() {
    mCounter++;
    updateViewModel(getViewModel());
  }

  public void onSecondIncrementButtonClicked() {
    mSecondCounter++;
    updateViewModel(getViewModel());
  }

  private void updateViewModel(@NotNull final MainViewModel mainViewModel) {
    mainViewModel.setCounterText(String.valueOf(mCounter));
    mainViewModel.setSecondCounterText(String.valueOf(mSecondCounter));
    mainViewModel.notifyObservers();
  }
}
