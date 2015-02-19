package com.nhaarman.gable.screen;

import com.nhaarman.gable.TestComponent;
import com.nhaarman.gable.TestPresenter;
import com.nhaarman.gable.TestScreen;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ScreenTest {

  private TestScreen mTestScreen;

  @Before
  public void setUp() {
    mTestScreen = new TestScreen();
  }

  @Test
  public void getPresenter_returnsSingleInstance() {
    /* Given */
    TestPresenter firstPresenter = mTestScreen.getPresenter(new TestComponent());

    /* When */
    TestPresenter secondPresenter = mTestScreen.getPresenter(new TestComponent());

    /* Then */
    assertThat(firstPresenter, is(secondPresenter));
  }
}