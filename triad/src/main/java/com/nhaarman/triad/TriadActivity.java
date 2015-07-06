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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * An {@link Activity} which is the root of an application that uses Triad.
 *
 * @param <M> The {@code main component} to use for {@link Presenter} creation.
 */
public abstract class TriadActivity<M> extends Activity {

  @NonNull
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

  @NonNull
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
  @NonNull
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
  @NonNull
  protected Triad getTriad() {
    return mDelegate.getTriad();
  }

  protected void setOnScreenChangedListener(@Nullable final OnScreenChangedListener<M> onScreenChangedListener) {
    mDelegate.setOnScreenChangedListener(onScreenChangedListener);
  }
}