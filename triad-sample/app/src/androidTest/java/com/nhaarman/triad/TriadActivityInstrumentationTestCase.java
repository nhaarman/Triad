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
