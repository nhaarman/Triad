package com.nhaarman.triad.sample.main;

import java.util.Observable;
import org.jetbrains.annotations.NotNull;

class MainViewModel extends Observable {

  @NotNull
  private String mCounterText;

  @NotNull
  private String mSecondCounterText;

  @NotNull
  public String getCounterText() {
    return mCounterText;
  }

  public void setCounterText(@NotNull final String counterText) {
    mCounterText = counterText;
    setChanged();
  }

  @NotNull
  public String getSecondCounterText() {
    return mSecondCounterText;
  }

  public void setSecondCounterText(@NotNull final String secondCounterText) {
    mSecondCounterText = secondCounterText;
    setChanged();
  }
}
