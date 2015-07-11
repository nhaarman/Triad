package com.nhaarman.triad;

import android.support.annotation.NonNull;

public class TestPresenter extends ScreenPresenter<TestPresenter, TestRelativeLayoutContainer> {

  public boolean onControlGainedCalled;

  public boolean onControlLostCalled;

  @Override
  protected void onControlGained(@NonNull final TestRelativeLayoutContainer container) {
    onControlGainedCalled = true;
  }

  @Override
  protected void onControlLost() {
    onControlLostCalled = true;
  }

  @NonNull
  @Override
  public Triad getTriad() {
    return super.getTriad();
  }
}