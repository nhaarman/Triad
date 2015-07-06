package com.nhaarman.triad;

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
    Triad triad = Triad.emptyInstance();
    mScreenPresenter.setTriad(triad);

    /* When */
    Triad result = mScreenPresenter.getTriad();

    /* Then */
    assertThat(result, is(triad));
  }

  @Test(expected = IllegalStateException.class)
  public void getFlow_withoutSettedFlow_throwsException() {
    /* When */
    mScreenPresenter.getTriad();
  }
}