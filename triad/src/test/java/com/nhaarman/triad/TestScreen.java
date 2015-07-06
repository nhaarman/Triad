package com.nhaarman.triad;

import android.support.annotation.NonNull;

public class TestScreen extends Screen<TestPresenter, TestRelativeLayoutContainer, TestComponent> {

  @Override
  protected int getLayoutResId() {
    return 0;
  }

  @NonNull
  @Override
  protected TestPresenter createPresenter(@NonNull final TestComponent testComponent) {
    return new TestPresenter();
  }
}
