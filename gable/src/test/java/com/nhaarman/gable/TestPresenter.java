package com.nhaarman.gable;

import com.nhaarman.gable.presenter.ScreenPresenter;
import org.jetbrains.annotations.NotNull;

public class TestPresenter extends ScreenPresenter<TestPresenter, TestRelativeLayoutContainer> {

  public boolean onControlGainedCalled;
  public boolean onControlLostCalled;

  @Override
  protected void onControlGained(@NotNull final TestRelativeLayoutContainer container) {
    onControlGainedCalled = true;
  }

  @Override
  protected void onControlLost() {
    onControlLostCalled = true;
  }
}