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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class RelativeLayoutScreenContainer<
    ActivityComponent,
    P extends ScreenPresenter<ActivityComponent, P, C>,
    C extends ScreenContainer<ActivityComponent, P, C>
    > extends RelativeLayoutContainer<P, C> implements ScreenContainer<ActivityComponent, P, C> {

  @Nullable
  private ActivityComponent mActivityComponent;

  public RelativeLayoutScreenContainer(final Context context) {
    super(context);
  }

  public RelativeLayoutScreenContainer(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public RelativeLayoutScreenContainer(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  public RelativeLayoutScreenContainer(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @NonNull
  public ActivityComponent getActivityComponent() {
    if (mActivityComponent == null) {
      throw new NullPointerException("ActivityComponent has not been set for " + getClass().getCanonicalName());
    }

    return mActivityComponent;
  }

  @Override
  public void setActivityComponent(@NonNull final ActivityComponent activityComponent) {
    mActivityComponent = activityComponent;
  }

  @Override
  void acquire() {
    P presenter = getPresenter();
    presenter.acquire((C) this, getActivityComponent());
  }
}
