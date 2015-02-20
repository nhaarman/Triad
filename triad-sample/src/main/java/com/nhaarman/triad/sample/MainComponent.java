package com.nhaarman.triad.sample;

import flow.Flow;
import org.jetbrains.annotations.NotNull;

public class MainComponent {

  @NotNull
  private final Flow mFlow;

  public MainComponent(@NotNull final Flow flow) {
    mFlow = flow;
  }

  @NotNull
  public Flow flow() {
    return mFlow;
  }
}
