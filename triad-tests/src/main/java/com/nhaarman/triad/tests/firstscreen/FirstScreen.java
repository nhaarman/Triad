package com.nhaarman.triad.tests.firstscreen;

import com.nhaarman.triad.Screen;
import com.nhaarman.triad.tests.R;
import com.nhaarman.triad.tests.TestComponent;
import org.jetbrains.annotations.NotNull;

public class FirstScreen extends Screen<FirstScreenPresenter, FirstScreenContainer, TestComponent> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_screen_first;
  }

  @NotNull
  @Override
  protected FirstScreenPresenter createPresenter(@NotNull final TestComponent testComponent) {
    return new FirstScreenPresenter();
  }
}
