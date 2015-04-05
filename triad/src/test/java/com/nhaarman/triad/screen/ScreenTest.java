package com.nhaarman.triad.screen;

import com.nhaarman.triad.TestComponent;
import com.nhaarman.triad.TestPresenter;
import com.nhaarman.triad.TestScreen;
import flow.Flow;
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
    TestPresenter firstPresenter = mTestScreen.getPresenter(new TestComponent(), new Flow(null, null));

    /* When */
    TestPresenter secondPresenter = mTestScreen.getPresenter(new TestComponent(), new Flow(null, null));

    /* Then */
    assertThat(firstPresenter, is(secondPresenter));
  }

  @Test
  public void getPresenter_returnsInstanceWithFlowSet() {
    /* Given */
    Flow flow = new Flow(null, null);

    /* When */
    TestPresenter presenter = mTestScreen.getPresenter(new TestComponent(), flow);

    /* Then */
    assertThat(presenter.getFlow(), is(flow));
  }
}