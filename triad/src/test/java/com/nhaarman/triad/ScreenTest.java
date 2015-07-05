package com.nhaarman.triad;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ScreenTest {

  private TestScreen mTestScreen;

  private Triad mTriadMock;

  @Before
  public void setUp() {
    mTestScreen = new TestScreen();

    mTriadMock = mock(Triad.class);
  }

  @Test
  public void getPresenter_returnsSingleInstance() {
    /* Given */
    TestPresenter firstPresenter = mTestScreen.getPresenter(new TestComponent(),mTriadMock);

    /* When */
    TestPresenter secondPresenter = mTestScreen.getPresenter(new TestComponent(), mTriadMock);

    /* Then */
    assertThat(firstPresenter, is(secondPresenter));
  }

  @Test
  public void getPresenter_returnsInstanceWithFlowSet() {
    /* When */
    TestPresenter presenter = mTestScreen.getPresenter(new TestComponent(), mTriadMock);

    /* Then */
    assertThat(presenter.getTriad(), is(mTriadMock));
  }
}