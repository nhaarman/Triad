package com.nhaarman.triad;

import com.nhaarman.triad.tests.secondscreen.SecondScreen;

import static com.nhaarman.triad.utils.ViewWaiter.viewNotPresent;
import static com.nhaarman.triad.utils.ViewWaiter.waitUntil;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class RotationScreenTransitionTest extends TestActivityInstrumentationTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    getInstrumentation().runOnMainSync(new Runnable() {
      @Override
      public void run() {
        mTriad.goTo(new SecondScreen());
      }
    });
    waitUntil(viewNotPresent(mScreenHolder, com.nhaarman.triad.tests.R.id.view_screen_first));
    rotate();
  }

  public void test_afterRotation_secondScreenIsPresent() {
    assertThat(mScreenHolder.findViewById(com.nhaarman.triad.tests.R.id.view_screen_second), is(not(nullValue())));
    assertThat(mScreenHolder.findViewById(com.nhaarman.triad.tests.R.id.view_screen_first), is(nullValue()));
  }

  public void test_afterRotationAndBackTransition_firstScreenIsPresent() throws InterruptedException {
    getInstrumentation().runOnMainSync(new Runnable() {
      @Override
      public void run() {
        mTriad.goBack();
      }
    });
    waitUntil(viewNotPresent(mScreenHolder, com.nhaarman.triad.tests.R.id.view_screen_second));

    assertThat(mScreenHolder.findViewById(com.nhaarman.triad.tests.R.id.view_screen_first), is(not(nullValue())));
    assertThat(mScreenHolder.findViewById(com.nhaarman.triad.tests.R.id.view_screen_second), is(nullValue()));
  }
}
