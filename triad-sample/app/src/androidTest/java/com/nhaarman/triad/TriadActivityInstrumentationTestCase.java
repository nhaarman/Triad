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
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.support.test.internal.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.test.ActivityInstrumentationTestCase2;
import java.util.Collection;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class TriadActivityInstrumentationTestCase<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

  private T mActivity;

  public TriadActivityInstrumentationTestCase(final Class<T> activityClass) {
    super(activityClass);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    getInstrumentation().callApplicationOnCreate((Application) getInstrumentation().getTargetContext().getApplicationContext());
  }

  protected void createNote(final String title, final String contents) throws InterruptedException {
    onView(withText(com.nhaarman.triad.sample.R.string.create_note)).perform(click());
    onView(withHint(com.nhaarman.triad.sample.R.string.title)).perform(typeText(title));
    onView(withHint(com.nhaarman.triad.sample.R.string.contents)).perform(typeText(contents));
    onView(withText(com.nhaarman.triad.sample.R.string.save)).perform(click());
    getInstrumentation().waitForIdleSync();
  }

  protected void rotate() throws InterruptedException {
    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    getInstrumentation().waitForIdleSync();
  }

  @Override
  public T getActivity() {
    getInstrumentation().runOnMainSync(new Runnable() {
      @Override
      public void run() {
        Collection<Activity> activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
        if (activities.isEmpty()) {
          mActivity = null;
        } else {
          mActivity = (T) activities.iterator().next();
        }
      }
    });
    getInstrumentation().waitForIdleSync();

    return mActivity == null ? super.getActivity() : mActivity;
  }
}
