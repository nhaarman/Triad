package com.nhaarman.gable.container;

import com.nhaarman.gable.presenter.Presenter;
import org.jetbrains.annotations.NotNull;

public interface Container<P extends Presenter<P, C>, C extends Container<P, C>> {

  void setPresenter(@NotNull P presenter);
}
