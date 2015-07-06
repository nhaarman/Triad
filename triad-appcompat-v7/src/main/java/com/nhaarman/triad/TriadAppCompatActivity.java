/*
 * Copyright 2015 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.triad;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link Activity} which is the root of an application that uses Triad.
 *
 * @param <M> The {@code main component} to use for {@link Presenter} creation.
 */
public abstract class TriadAppCompatActivity<M> extends AppCompatActivity {

  @NotNull
  private final TriadDelegate<M> mTriadDelegate;

  public TriadAppCompatActivity() {
    mTriadDelegate = new TriadDelegate<>(this);
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mTriadDelegate.onCreate(createMainComponent());
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
    mTriadDelegate.onStart();
  }

  @Override
  protected void onPostCreate(final Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mTriadDelegate.onPostCreate();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mTriadDelegate.onStop();
  }

  @Override
  public void onBackPressed() {
    if (!mTriadDelegate.onBackPressed()) {
      super.onBackPressed();
    }
  }

  /**
   * Returns the {@link Triad} instance to be used to navigate between {@link Screen}s.
   */
  @NotNull
  protected Triad getFlow() {
    return mTriadDelegate.getTriad();
  }

  protected void setOnScreenChangedListener(@Nullable final OnScreenChangedListener<M> onScreenChangedListener) {
    mTriadDelegate.setOnScreenChangedListener(onScreenChangedListener);
  }
}
