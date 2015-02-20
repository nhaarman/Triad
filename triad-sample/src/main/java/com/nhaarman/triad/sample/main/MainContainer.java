package com.nhaarman.triad.sample.main;

import com.nhaarman.triad.container.ScreenContainer;
import org.jetbrains.annotations.NotNull;

interface MainContainer extends ScreenContainer<MainPresenter, MainContainer> {

  void setViewModel(@NotNull final MainViewModel viewModel);
}
