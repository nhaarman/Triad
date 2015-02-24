package com.nhaarman.triad;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import com.nhaarman.triad.sample.*;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class TraidActivityInstrumentationTestCase<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

  public TraidActivityInstrumentationTestCase(final Class<T> activityClass) {
    super(activityClass);
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    FlowManager.destroyInstance();
  }

  protected void createNote(final String title, final String contents) throws InterruptedException {
    onView(withText(com.nhaarman.triad.sample.R.string.create_note)).perform(click());
    onView(withHint(com.nhaarman.triad.sample.R.string.title)).perform(typeText(title));
    onView(withHint(com.nhaarman.triad.sample.R.string.contents)).perform(typeText(contents));
    onView(withText(com.nhaarman.triad.sample.R.string.save)).perform(click());
    getInstrumentation().waitForIdleSync();
    Thread.sleep(500);
  }
}
