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
    TestRelativeLayoutScreenContainer container = mock(TestRelativeLayoutScreenContainer.class);

    /* When */
    mPresenter.acquire(container, mock(ActivityComponent.class));

    /* Then */
    assertThat(mPresenter.getContainer().isPresent(), is(true));
  }

  @Test
  public void afterReleasingContainer_theContainerIsNotPresent() {
    /* Given */
    TestRelativeLayoutScreenContainer container = mock(TestRelativeLayoutScreenContainer.class);
    mPresenter.acquire(container, mock(ActivityComponent.class));

    /* When */
    mPresenter.releaseContainer();

    /* Then */
    assertThat(mPresenter.getContainer().isPresent(), is(false));
  }

  @Test
  public void afterAcquiringContainer_onControlGainedIsCalled() {
    /* Given */
    TestRelativeLayoutScreenContainer container = mock(TestRelativeLayoutScreenContainer.class);

    /* When */
    mPresenter.acquire(container, mock(ActivityComponent.class));

    /* Then */
    assertThat(mPresenter.onControlGainedCalled, is(true));
    assertThat(mPresenter.onControlLostCalled, is(false));
  }

  @Test
  public void afterReleasingContainer_onControlLostIsCalled() {
    /* Given */
    TestRelativeLayoutScreenContainer container = mock(TestRelativeLayoutScreenContainer.class);
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
    TestRelativeLayoutScreenContainer container = mock(TestRelativeLayoutScreenContainer.class);
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
    TestRelativeLayoutScreenContainer container1 = mock(TestRelativeLayoutScreenContainer.class);
    TestRelativeLayoutScreenContainer container2 = mock(TestRelativeLayoutScreenContainer.class);

    mPresenter.acquire(container1, mock(ActivityComponent.class));
    mPresenter.onControlLostCalled = false;
    mPresenter.onControlGainedCalled = false;

    /* When */
    mPresenter.acquire(container2, mock(ActivityComponent.class));

    /* Then */
    assertThat(mPresenter.onControlLostCalled, is(true));
    assertThat(mPresenter.onControlGainedCalled, is(true));
  }
}