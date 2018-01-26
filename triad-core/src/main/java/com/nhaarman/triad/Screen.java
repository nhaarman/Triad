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
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.nhaarman.triad.Preconditions.checkNotNull;

public abstract class Screen<ApplicationComponent> {

    @NonNull
    private final SparseArray<Presenter<?>> presenters = new SparseArray<>();

    @NonNull
    private final SparseArray<Parcelable> state = new SparseArray<>();

    @Nullable
    private ApplicationComponent applicationComponent;

    @LayoutRes
    protected abstract int getLayoutResId();

    @NonNull
    protected View createView(@NonNull final ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(), parent, false);
    }

    @NonNull
    public Presenter<?> getPresenter(final int viewId) {
        Presenter<?> presenter = presenters.get(viewId);

        if (presenter == null) {
            presenter = createPresenter(viewId);
            presenters.put(viewId, presenter);
        }

        return presenter;
    }

    @NonNull
    protected abstract Presenter<?> createPresenter(int viewId);

    @NonNull
    protected final ApplicationComponent getApplicationComponent() {
        return checkNotNull(applicationComponent, "Application component is null.");
    }

    final void setApplicationComponent(@Nullable final ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    final void saveState(@NonNull final View view) {
        view.saveHierarchyState(state);
    }

    final void restoreState(@NonNull final View view) {
        view.restoreHierarchyState(state);
    }

    @SuppressWarnings("NoopMethodInAbstractClass")
    protected void onCreate() {
    }

    @SuppressWarnings({"NoopMethodInAbstractClass", "UnusedParameters"})
    protected void onAttach(@NonNull final Activity activity) {
    }

    protected boolean onBackPressed() {
        return false;
    }

    @SuppressWarnings({"NoopMethodInAbstractClass", "UnusedParameters"})
    protected void onDetach(@NonNull final Activity activity) {
    }

    @SuppressWarnings("NoopMethodInAbstractClass")
    protected void onDestroy() {
    }
}
