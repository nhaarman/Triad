package com.nhaarman.triad;

import static org.assertj.android.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class InitialStateTest extends TestActivityInstrumentationTestCase {

  public void test_initially_screenHolderVisible() {
    assertThat(mScreenHolder).isVisible();
  }

  public void test_initially_dimmerView_isVisible() {
    assertThat(mDimmerView).isVisible();
  }

  public void test_initially_dimmerView_isFullyTranslucent() {
    assertThat(mDimmerView).hasAlpha(0f);
  }

  public void test_initially_dimmerView_isNotClickable() {
    assertThat(mDimmerView).isNotClickable();
  }

  public void test_initially_dialogHolder_isVisible() {
    assertThat(mDialogHolder).isVisible();
  }

  public void test_initially_screenHolderContainsFirstScreen() {
    assertThat(mScreenHolder.findViewById(com.nhaarman.triad.tests.R.id.view_screen_first), is(not(nullValue())));
  }

  public void test_initially_dialogHolderContainsNoChildren() {
    assertThat(mDialogHolder).hasChildCount(0);
  }
}
