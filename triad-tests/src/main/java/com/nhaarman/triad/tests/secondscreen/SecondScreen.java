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

package com.nhaarman.triad.tests.secondscreen;

import android.support.annotation.NonNull;
import com.nhaarman.triad.Screen;
import com.nhaarman.triad.tests.ActivityComponent;
import com.nhaarman.triad.tests.ApplicationComponent;
import com.nhaarman.triad.tests.R;

public class SecondScreen extends Screen<ApplicationComponent, ActivityComponent, SecondScreenPresenter, SecondScreenContainer> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_screen_second;
  }

  @NonNull
  @Override
  protected SecondScreenPresenter createPresenter(@NonNull final ApplicationComponent applicationComponent) {
    return new SecondScreenPresenter();
  }
}
