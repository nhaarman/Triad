package com.nhaarman.gable.presenter;

import com.nhaarman.gable.container.Container;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Presenter<P extends Presenter<P, C>, C extends Container<P, C>> {

  @Nullable
  private C mContainer;

  protected void onControlGained(@NotNull final C container) {
  }

  public void acquire(@NotNull final C container) {
    mContainer = container;
    onControlGained(container);
  }

  public void releaseContainer() {
    mContainer = null;
    onControlLost();
  }

  protected void onControlLost() {
  }

  @Nullable
  public C getContainer() {
    return mContainer;
  }
}
