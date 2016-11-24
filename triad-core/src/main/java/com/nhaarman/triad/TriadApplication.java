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

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.nhaarman.triad.Preconditions.checkState;

/**
 * A base {@link Application} implementation that handles creation of the {@link Triad} and
 * {@code ApplicationComponent} instance.
 */
public abstract class TriadApplication<ApplicationComponent> extends Application
      implements TriadProvider, ApplicationComponentProvider<ApplicationComponent> {

    @Nullable
    private Triad mTriad;

    @Nullable
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mTriad = TriadFactory.emptyInstance();
        mApplicationComponent = createApplicationComponent();
    }

    /**
     * Creates a new instance of the {@code ApplicationComponent}.
     */
    @NonNull
    protected abstract ApplicationComponent createApplicationComponent();

    @Override
    @NonNull
    public final Triad getTriad() {
        checkState(mTriad != null, "Calling getTriad() before onCreate().");

        return mTriad;
    }

    @NonNull
    @Override
    public final ApplicationComponent getApplicationComponent() {
        checkState(mApplicationComponent != null, "Calling getApplicationComponent() before onCreate().");

        return mApplicationComponent;
    }
}
