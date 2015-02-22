package com.nhaarman.triad;

import com.nhaarman.triad.tests.firstdialog.FirstDialogScreen;

import static com.nhaarman.triad.utils.ViewWaiter.viewNotPresent;
import static com.nhaarman.triad.utils.ViewWaiter.viewVisible;
import static com.nhaarman.triad.utils.ViewWaiter.waitUntil;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class RotationDialogTransitionTest extends TestActivityInstrumentationTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    getInstrumentation().runOnMainSync(new Runnable() {
      @Override
      public void run() {
        mFlow.goTo(new FirstDialogScreen());
      }
    });
    waitUntil(viewVisible(mDialogHolder, com.nhaarman.triad.tests.R.id.view_dialog_first));
    rotate();
  }

  public void test_afterRotation_screenIsPresent() {
    assertThat(mScreenHolder.findViewById(com.nhaarman.triad.tests.R.id.view_screen_first), is(not(nullValue())));
  }

  public void test_afterRotation_dialogIsPresent() {
    assertThat(mDialogHolder.findViewById(com.nhaarman.triad.tests.R.id.view_dialog_first), is(not(nullValue())));
  }

  public void test_afterRotationAndBackTransition_firstScreenIsPresent() throws InterruptedException {
    getInstrumentation().runOnMainSync(new Runnable() {
      @Override
      public void run() {
        mFlow.goBack();
      }
    });
    waitUntil(viewNotPresent(mScreenHolder, com.nhaarman.triad.tests.R.id.view_dialog_first));

    assertThat(mScreenHolder.findViewById(com.nhaarman.triad.tests.R.id.view_screen_first), is(not(nullValue())));
    assertThat(mDialogHolder.findViewById(com.nhaarman.triad.tests.R.id.view_dialog_first), is(nullValue()));
  }
}
