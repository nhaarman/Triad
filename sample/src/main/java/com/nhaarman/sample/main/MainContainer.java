package com.nhaarman.sample.main;

import com.nhaarman.gable.container.ScreenContainer;
import org.jetbrains.annotations.NotNull;

interface MainContainer extends ScreenContainer<MainPresenter, MainContainer> {

  void setViewModel(@NotNull final MainViewModel viewModel);
}
