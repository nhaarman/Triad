package com.nhaarman.triad;

import android.widget.TextView;
import com.nhaarman.triad.tests.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.assertj.android.api.Assertions.assertThat;

public class RotationStateTest extends TestActivityInstrumentationTestCase {

  public void test_initially_theCounterTextIsZero() {
    assertThat(getCounterTV()).hasText("0");
  }

  public void test_afterRotation_theCounterTextIsStillZero() {
    rotate();
    assertThat(getCounterTV()).hasText("0");
  }

  public void test_afterIncrement_theCounterTextIsOne() {
    onView(withId(R.id.view_screen_first_button)).perform(click());
    getInstrumentation().waitForIdleSync();
    assertThat(getCounterTV()).hasText("1");
  }

  public void test_afterIncrementAndRotation_theCounterTextIsOne() {
    onView(withId(R.id.view_screen_first_button)).perform(click());
    rotate();
    assertThat(getCounterTV()).hasText("1");
  }

  private TextView getCounterTV() {
    return (TextView) mActivity.findViewById(R.id.view_screen_first_tv);
  }
}
