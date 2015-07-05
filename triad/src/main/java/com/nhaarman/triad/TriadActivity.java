package com.nhaarman.triad;

import android.app.Activity;
import android.os.Bundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link Activity} which is the root of an application that uses Triad.
 *
 * @param <M> The {@code main component} to use for {@link Presenter} creation.
 */
public abstract class TriadActivity<M> extends Activity {

  @NotNull
  private final TriadDelegate<M> mDelegate;

  @Nullable
  private M mActivityComponent;

  public TriadActivity() {
    mDelegate = new TriadDelegate<>(this);
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDelegate.onCreate(getActivityComponent());
  }

  @NotNull
  protected synchronized M getActivityComponent() {
    if (mActivityComponent == null) {
      mActivityComponent = createActivityComponent();
    }

    return mActivityComponent;
  }

  /**
   * Creates the main component which is used to retrieve dependencies from that are needed to create {@link Presenter}s.
   *
   * @return The created main component.
   */
  @NotNull
  protected abstract M createActivityComponent();

  @Override
  protected void onStart() {
    super.onStart();
    mDelegate.onStart();
  }

  @Override
  protected void onPostCreate(final Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mDelegate.onPostCreate();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mDelegate.onStop();
  }

  @Override
  public void onBackPressed() {
    if (!mDelegate.onBackPressed()) {
      super.onBackPressed();
    }
  }

  /**
   * Returns the {@link Triad} instance to be used to navigate between {@link Screen}s.
   */
  @NotNull
  protected Triad getTriad() {
    return mDelegate.getTriad();
  }

  protected void setOnScreenChangedListener(@Nullable final OnScreenChangedListener<M> onScreenChangedListener) {
    mDelegate.setOnScreenChangedListener(onScreenChangedListener);
  }
}