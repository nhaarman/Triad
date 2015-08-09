package com.nhaarman.triad;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
public class PresenterTest {

  private TestPresenter mPresenter;

  @Before
  public void setUp() {
    mPresenter = new TestPresenter();
  }

  @Test
  public void initially_theContainerIsPresent() {
    assertThat(mPresenter.getContainer().isPresent(), is(false));
  }

  @Test
  public void afterAcquiringContainer_theContainerIsPresent() {
    /* Given */
    TestRelativeLayoutContainer container = mock(TestRelativeLayoutContainer.class);

    /* When */
    mPresenter.acquire(container, mock(ActivityComponent.class));

    /* Then */
    assertThat(mPresenter.getContainer().isPresent(), is(true));
  }

  @Test
  public void afterReleasingContainer_theContainerIsNotPresent() {
    /* Given */
    TestRelativeLayoutContainer container = mock(TestRelativeLayoutContainer.class);
    mPresenter.acquire(container, mock(ActivityComponent.class));

    /* When */
    mPresenter.releaseContainer();

    /* Then */
    assertThat(mPresenter.getContainer().isPresent(), is(false));
  }

  @Test
  public void afterAcquiringContainer_onControlGainedIsCalled() {
    /* Given */
    TestRelativeLayoutContainer container = mock(TestRelativeLayoutContainer.class);

    /* When */
    mPresenter.acquire(container, mock(ActivityComponent.class));

    /* Then */
    assertThat(mPresenter.onControlGainedCalled, is(true));
    assertThat(mPresenter.onControlLostCalled, is(false));
  }

  @Test
  public void afterReleasingContainer_onControlLostIsCalled() {
    /* Given */
    TestRelativeLayoutContainer container = mock(TestRelativeLayoutContainer.class);
    mPresenter.acquire(container, mock(ActivityComponent.class));
    mPresenter.onControlGainedCalled = false;

    /* When */
    mPresenter.releaseContainer();

    /* Then */
    assertThat(mPresenter.onControlLostCalled, is(true));
    assertThat(mPresenter.onControlGainedCalled, is(false));
  }

  @Test
  public void acquiringTheSameContainerTwice_doesNothing() {
    /* Given */
    TestRelativeLayoutContainer container = mock(TestRelativeLayoutContainer.class);
    mPresenter.acquire(container, mock(ActivityComponent.class));
    mPresenter.onControlLostCalled = false;
    mPresenter.onControlGainedCalled = false;

    /* When */
    mPresenter.acquire(container, mock(ActivityComponent.class));

    /* Then */
    assertThat(mPresenter.onControlLostCalled, is(false));
    assertThat(mPresenter.onControlGainedCalled, is(false));
  }

  @Test
  public void acquiringAnotherContainer_firstReleasesTheCurrentContainer() {
    /* Given */
    TestRelativeLayoutContainer container1 = mock(TestRelativeLayoutContainer.class);
    TestRelativeLayoutContainer container2 = mock(TestRelativeLayoutContainer.class);

    mPresenter.acquire(container1, mock(ActivityComponent.class));
    mPresenter.onControlLostCalled = false;
    mPresenter.onControlGainedCalled = false;

    /* When */
    mPresenter.acquire(container2, mock(ActivityComponent.class));

    /* Then */
    assertThat(mPresenter.onControlLostCalled, is(true));
    assertThat(mPresenter.onControlGainedCalled, is(true));
  }

  @Test
  public void getFlow_returnsSettedFlow() {
    /* Given */
    Triad triad = Triad.emptyInstance();
    mPresenter.setTriad(triad);

    /* When */
    Triad result = mPresenter.getTriad();

    /* Then */
    assertThat(result, is(triad));
  }

  @Test(expected = IllegalStateException.class)
  public void getFlow_withoutSettedFlow_throwsException() {
    /* When */
    mPresenter.getTriad();
  }
}