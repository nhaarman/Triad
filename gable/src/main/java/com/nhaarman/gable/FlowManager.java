package com.nhaarman.gable;

import flow.Backstack;
import flow.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class FlowManager {

  @Nullable
  private static FlowManager sFlowManager;

  @NotNull
  private final Flow mFlow;

  private Flow.Listener mFlowListener;

  private FlowManager(@NotNull final Backstack backstack) {
    mFlow = new Flow(backstack, new MyFlowListener());
  }

  @NotNull
  public Flow getFlow() {
    return mFlow;
  }

  public void setFlowListener(final Flow.Listener flowListener) {
    mFlowListener = flowListener;
  }

  public static synchronized FlowManager getInstance(@NotNull final Backstack backstack) {
    if (sFlowManager == null) {
      sFlowManager = new FlowManager(backstack);
    }
    return sFlowManager;
  }

  private class MyFlowListener implements Flow.Listener {

    @Override
    public void go(final Backstack nextBackstack, final Flow.Direction direction, final Flow.Callback callback) {
      if (mFlowListener == null) {
        throw new NullPointerException("Set a listener!");
      }
      mFlowListener.go(nextBackstack, direction, callback);
    }
  }
}
