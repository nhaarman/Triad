package com.nhaarman.gable.container;

import com.nhaarman.gable.presenter.ScreenPresenter;

public interface ScreenContainer<P extends ScreenPresenter<P, C>, C extends Container<P, C>> extends Container<P, C> {

}
