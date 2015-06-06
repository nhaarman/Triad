package com.nhaarman.triad.presenter;

import com.nhaarman.triad.ScreenPresenter;
import flow.Flow;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ScreenPresenterTest {

  private ScreenPresenter mScreenPresenter;

  @Before
  public void setUp() {
    mScreenPresenter = new ScreenPresenter() {
    };
  }

  @Test
  public void getFlow_returnsSettedFlow() {
    /* Given */
    Flow flow = new Flow(null, null);
    mScreenPresenter.setFlow(flow);

    /* When */
    Flow result = mScreenPresenter.getFlow();

    /* Then */
    assertThat(result, is(flow));
  }

  @Test(expected = IllegalStateException.class)
  public void getFlow_withoutSettedFlow_throwsException() {
    /* When */
    mScreenPresenter.getFlow();
  }

}