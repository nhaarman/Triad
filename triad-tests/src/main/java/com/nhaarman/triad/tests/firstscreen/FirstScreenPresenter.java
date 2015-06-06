package com.nhaarman.triad.tests.firstscreen;

import com.nhaarman.triad.ScreenPresenter;
import org.jetbrains.annotations.NotNull;

public class FirstScreenPresenter extends ScreenPresenter<FirstScreenPresenter, FirstScreenContainer> {

  private int mCounter = 0;

  @Override
  protected void onControlGained(@NotNull final FirstScreenContainer container) {
    getContainer().setText(String.valueOf(mCounter));
  }

  public void onButtonClicked() {
    mCounter++;
    getContainer().setText(String.valueOf(mCounter));
  }
}
