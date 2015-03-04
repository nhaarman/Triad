package com.nhaarman.triad.sample;

import flow.Backstack;
import flow.Flow;

@SuppressWarnings("all")
public class MyFlowListener implements Flow.Listener {

  public Object lastScreen;
  public Flow.Direction lastDirection;

  @Override
  public void go(final Backstack nextBackstack, final Flow.Direction direction, final Flow.Callback callback) {
    lastScreen = nextBackstack.current().getScreen();
    lastDirection = direction;
  }
}