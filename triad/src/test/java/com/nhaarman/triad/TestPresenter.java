package com.nhaarman.triad;

import android.support.annotation.NonNull;

public class TestPresenter extends Presenter<ActivityComponent, TestPresenter, TestRelativeLayoutContainer> {

  public boolean onControlGainedCalled;

  public boolean onControlLostCalled;

  @Override
  protected void onControlGained(@NonNull final TestRelativeLayoutContainer container, @NonNull final ActivityComponent activityComponent) {
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