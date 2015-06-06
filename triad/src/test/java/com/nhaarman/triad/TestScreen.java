package com.nhaarman.triad;

import org.jetbrains.annotations.NotNull;

public class TestScreen extends Screen<TestPresenter, TestRelativeLayoutContainer, TestComponent> {

  @Override
  protected int getLayoutResId() {
    return 0;
  }

  @NotNull
  @Override
  protected TestPresenter createPresenter(@NotNull final TestComponent testComponent) {
    return new TestPresenter();
  }
}
