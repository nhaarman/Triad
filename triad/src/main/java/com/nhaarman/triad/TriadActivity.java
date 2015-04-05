package com.nhaarman.triad;

import android.app.Activity;
import android.os.Bundle;
import com.nhaarman.triad.presenter.Presenter;
import com.nhaarman.triad.screen.Screen;
import flow.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link Activity} which is the root of an application that uses Triad.
 *
 * @param <M> The {@code main component} to use for {@link Presenter} creation.
 */
public abstract class TriadActivity<M> extends Activity {

  @NotNull
  private final TriadManager<M> mTriadManager;

  public TriadActivity() {
    mTriadManager = new TriadManager<>(this);
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mTriadManager.onCreate(createInitialScreen(), createMainComponent());
  }

  /**
   * Creates the main component which is used to retrieve dependencies from that are needed to create {@link Presenter}s.
   *
   * @return The created main component.
   */
  @NotNull
  protected abstract M createMainComponent();

  /**
   * Creates the {@link Screen} that is to be shown when the application starts.
   *
   * @return The created {@link Screen}.
   */
  @NotNull
  protected abstract Screen<?, ?, M> createInitialScreen();

  @Override
  protected void onStart() {
    super.onStart();
    mTriadManager.onStart();
  }

  @Override
  protected void onPostCreate(final Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mTriadManager.onPostCreate();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mTriadManager.onStop();
  }

  @Override
  public void onBackPressed() {
    if (!mTriadManager.onBackPressed()) {
      super.onBackPressed();
    }
  }

  /**
   * Returns the {@link Flow} instance to be used to navigate between {@link Screen}s.
   */
  @NotNull
  protected Flow getFlow() {
    return mTriadManager.getFlow();
  }

  protected void setOnScreenChangedListener(@Nullable final OnScreenChangedListener<M> onScreenChangedListener) {
    mTriadManager.setOnScreenChangedListener(onScreenChangedListener);
  }
}