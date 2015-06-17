package com.nhaarman.triad;

import static org.assertj.android.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.number.IsCloseTo.closeTo;

public class InitialStateTest extends TestActivityInstrumentationTestCase {

  public void test_initially_screenHolderVisible() {
    assertThat(mScreenHolder).isVisible();
  }

  public void test_initially_screenHolderContainsFirstScreen() {
    assertThat(mScreenHolder.findViewById(com.nhaarman.triad.tests.R.id.view_screen_first), is(not(nullValue())));
  }
}
