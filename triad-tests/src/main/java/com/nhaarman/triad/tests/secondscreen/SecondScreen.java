package com.nhaarman.triad.tests.secondscreen;

import com.nhaarman.triad.Screen;
import com.nhaarman.triad.tests.R;
import com.nhaarman.triad.tests.TestComponent;
import org.jetbrains.annotations.NotNull;

public class SecondScreen extends Screen<SecondScreenPresenter, SecondScreenContainer, TestComponent> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_screen_second;
  }

  @NotNull
  @Override
  protected SecondScreenPresenter createPresenter(@NotNull final TestComponent testComponent) {
    return new SecondScreenPresenter();
  }
}
