/*
 * Copyright 2016 Niek Haarman
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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * An {@link AppCompatActivity} which is the root of an application that uses Triad.
 *
 * @param <ApplicationComponent> The {@code ApplicationComponent} to use for {@code BasePresenter} creation.
 * @param <ActivityComponent>    The {@code ActivityComponent} to supply to {@code Presenters}.
 */
public abstract class TriadAppCompatActivity<ApplicationComponent, ActivityComponent> extends AppCompatActivity
      implements ScreenProvider<ApplicationComponent>, ActivityComponentProvider<ActivityComponent> {

    @NonNull
    private final TriadDelegate<ApplicationComponent> delegate;

    @Nullable
    private ActivityComponent activityComponent;

    public TriadAppCompatActivity() {
        delegate = TriadDelegate.createFor(this);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate.onCreate(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        delegate.onResume();
    }

    @Override
    @NonNull
    public synchronized ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = createActivityComponent();
        }

        return activityComponent;
    }

    /**
     * Creates the {@code ActivityComponent} which is used to retrieve dependencies from that are needed to create {@link BasePresenter}s.
     *
     * @return The created {@code ActivityComponent}.
     */
    @NonNull
    protected abstract ActivityComponent createActivityComponent();

    @NonNull
    @Override
    public Screen<ApplicationComponent> getCurrentScreen() {
        return delegate.getCurrentScreen();
    }

    @Override
    public void onBackPressed() {
        if (!delegate.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        delegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        delegate.onPause();
    }

    @Override
    protected void onDestroy() {
        delegate.onDestroy();
        super.onDestroy();
    }

    /**
     * Returns the {@link Triad} instance to be used to navigate between {@link Screen}s.
     */
    @NonNull
    protected Triad getTriad() {
        return delegate.getTriad();
    }

    protected void setOnScreenChangedListener(@Nullable final OnScreenChangedListener<ApplicationComponent> onScreenChangedListener) {
        delegate.setOnScreenChangedListener(onScreenChangedListener);
    }
}
