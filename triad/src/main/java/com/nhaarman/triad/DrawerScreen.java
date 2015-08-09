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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

public abstract class DrawerScreen<
    ApplicationComponent,
    ActivityComponent,
    P extends Presenter<ActivityComponent, P, C>,
    C extends Container<ActivityComponent, P, C>,
    DP extends Presenter<ActivityComponent, DP, DC>,
    DC extends Container<ActivityComponent, DP, DC>
    > extends Screen<ApplicationComponent, ActivityComponent, P, C> {

  @Nullable
  private DP mDrawerPresenter;

  @NonNull
  protected abstract DP createDrawerPresenter(@NonNull final ApplicationComponent component);

  /**
   * Returns the {@link P} that is tied to this {@code Screen} instance.
   * This instance is lazily instantiated.
   *
   * @param applicationComponent The {@code ApplicationComponent} to retrieve dependencies from.
   *
   * @return The {@link P}.
   */
  @NonNull
  private DP getDrawerPresenter(@NonNull final ApplicationComponent applicationComponent) {
    if (mDrawerPresenter == null) {
      mDrawerPresenter = createDrawerPresenter(applicationComponent);
    }

    return mDrawerPresenter;
  }

  @Override
  void acquirePresenter(@NonNull final ApplicationComponent applicationComponent,
                        @NonNull final ActivityComponent activityComponent,
                        @NonNull final Triad triad,
                        @NonNull final ViewGroup container) {
    DC dc = (DC) container.getChildAt(1);

    dc.setPresenterAndActivityComponent(getDrawerPresenter(applicationComponent), activityComponent);
    super.acquirePresenter(applicationComponent, activityComponent, triad, (ViewGroup) container.getChildAt(0));
  }
}
