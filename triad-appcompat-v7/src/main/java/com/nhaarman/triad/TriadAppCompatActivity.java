/*
 * Copyright 2015 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.triad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * An {@link Activity} which is the root of an application that uses Triad.
 *
 * @param <ApplicationComponent> The {@code ApplicationComponent} to use for {@code Presenter} creation.
 * @param <ActivityComponent> The {@code ActivityComponent} to supply to {@code Presenters}.
 */
public abstract class TriadAppCompatActivity<ApplicationComponent, ActivityComponent> extends AppCompatActivity {

  @NonNull
  private final TriadDelegate<ApplicationComponent, ActivityComponent> mDelegate;

  @Nullable
  private ActivityComponent mActivityComponent;

  public TriadAppCompatActivity() {
    mDelegate = new TriadDelegate<>(this);
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDelegate.onCreate(getActivityComponent());
  }

  @NonNull
  protected synchronized ActivityComponent getActivityComponent() {
    if (mActivityComponent == null) {
      mActivityComponent = createActivityComponent();
    }

    return mActivityComponent;
  }

  /**
   * Creates the {@code ActivityComponent} which is used to retrieve dependencies from that are needed to create {@link Presenter}s.
   *
   * @return The created {@code ActivityComponent}.
   */
  @NonNull
  protected abstract ActivityComponent createActivityComponent();

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

  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    mDelegate.onActivityResult(requestCode, resultCode, data);
  }

  /**
   * Returns the {@link Triad} instance to be used to navigate between {@link Screen}s.
   */
  @NonNull
  protected Triad getTriad() {
    return mDelegate.getTriad();
  }

  protected void setOnScreenChangedListener(@Nullable final OnScreenChangedListener<ApplicationComponent, ActivityComponent> onScreenChangedListener) {
    mDelegate.setOnScreenChangedListener(onScreenChangedListener);
  }
}
