package com.nhaarman.triad;

import android.support.annotation.NonNull;

public class TestPresenter extends ScreenPresenter<ActivityComponent, TestPresenter, TestRelativeLayoutScreenContainer> {

  public boolean onControlGainedCalled;

  public boolean onControlLostCalled;

  @Override
  protected void onControlGained(@NonNull final TestRelativeLayoutScreenContainer container, @NonNull final ActivityComponent activityComponent) {
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