package com.nhaarman.triad;

import android.support.annotation.NonNull;

public class TestScreen extends Screen<ApplicationComponent, ActivityComponent, TestPresenter, TestRelativeLayoutContainer> {

  @Override
  protected int getLayoutResId() {
    return 0;
  }

  @NonNull
  @Override
  protected TestPresenter createPresenter(@NonNull final ApplicationComponent applicationComponent) {
    return new TestPresenter();
  }
}
