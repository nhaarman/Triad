package com.nhaarman.triad;

import flow.Backstack;
import flow.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A singleton class which ensures only one {@link Flow} instance is used
 * throughout the lifetime of the application.
 */
class FlowManager {

  /**
   * The singleton instance.
   */
  @Nullable
  private static FlowManager sFlowManager;

  /**
   * The {@link Flow} instance.
   */
  @NotNull
  private final Flow mFlow;

  /**
   * The {@link Flow.Listener} that is used to notify of screen transitions.
   */
  @Nullable
  private Flow.Listener mFlowListener;

  /**
   * Creates a new {@code FlowManager} with given initial {@link Backstack}.
   *
   * @param backstack The {@link Backstack} to use.
   */
  private FlowManager(@NotNull final Backstack backstack) {
    mFlow = new Flow(backstack, new MyFlowListener());
  }

  /**
   * Returns the {@link Flow} instance.
   */
  @NotNull
  public Flow getFlow() {
    return mFlow;
  }

  /**
   * Sets the {@link Flow.Listener} that should be notified of pending screen transitions.
   */
  void setFlowListener(@Nullable final Flow.Listener flowListener) {
    mFlowListener = flowListener;
  }

  /**
   * Returns the {@code FlowManager} singleton instance.
   * If this method is called for the first time, given {@link Backstack} will be used as an
   * initial back stack.
   *
   * @param backstack The initial {@link Backstack} to use.
   */
  static synchronized FlowManager getInstance(@NotNull final Backstack backstack) {
    if (sFlowManager == null) {
      sFlowManager = new FlowManager(backstack);
    }
    return sFlowManager;
  }

  static synchronized void destroyInstance() {
    sFlowManager = null;
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
