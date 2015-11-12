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
  private final SparseArray<Presenter<?, ?>> mPresenters = new SparseArray<>();

  @NonNull
  private final SparseArray<Parcelable> mState = new SparseArray<>();

  @Nullable
  private ApplicationComponent mApplicationComponent;

  @LayoutRes
  protected abstract int getLayoutResId();

  @NonNull
  protected ViewGroup createView(@NonNull final ViewGroup parent) {
    return (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(), parent, false);
  }

  @NonNull
  public Presenter<?, ?> getPresenter(final int viewId) {
    if (mPresenters.get(viewId) == null) {
      mPresenters.put(viewId, createPresenter(viewId));
    }

    return mPresenters.get(viewId);
  }

  @NonNull
  protected abstract Presenter<?, ?> createPresenter(int viewId);

  boolean onBackPressed() {
    return false;
  }

  void setApplicationComponent(@Nullable final ApplicationComponent applicationComponent) {
    mApplicationComponent = applicationComponent;
  }

  @NonNull
  protected ApplicationComponent applicationComponent() {
    return checkNotNull(mApplicationComponent, "Application component is null.");
  }

  void saveState(@NonNull final ViewGroup view) {
    view.saveHierarchyState(mState);
  }

  void restoreState(@NonNull final ViewGroup view) {
    view.restoreHierarchyState(mState);
  }
}
