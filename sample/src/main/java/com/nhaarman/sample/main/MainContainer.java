package com.nhaarman.sample.main;

import com.nhaarman.gable.container.ScreenContainer;
import com.nhaarman.gable.container.ViewModelContainer;

interface MainContainer extends ScreenContainer<MainPresenter, MainContainer>, ViewModelContainer<MainViewModel> {
}
