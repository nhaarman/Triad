package com.nhaarman.gable.presenter;

import android.content.Context;
import android.util.AttributeSet;
import com.nhaarman.gable.container.RelativeLayoutContainer;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
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
  public void initially_theContainerIsNull() {
    assertThat(mPresenter.getContainer(), is(nullValue()));
  }

  @Test
  public void afterAcquiringContainer_theContainerIsNotNull() {
    /* Given */
    TestContainer container = mock(TestContainer.class);

    /* When */
    mPresenter.acquire(container);

    /* Then */
    assertThat(mPresenter.getContainer(), is(not(nullValue())));
  }

  @Test
  public void afterReleasingContainer_theContainerIsNull() {
    /* Given */
    TestContainer container = mock(TestContainer.class);
    mPresenter.acquire(container);

    /* When */
    mPresenter.releaseContainer();

    /* Then */
    assertThat(mPresenter.getContainer(), is(nullValue()));
  }

  @Test
  public void afterAcquiringContainer_onControlGainedIsCalled() {
    /* Given */
    TestContainer container = mock(TestContainer.class);

    /* When */
    mPresenter.acquire(container);

    /* Then */
    assertThat(mPresenter.onControlGainedCalled, is(true));
    assertThat(mPresenter.onControlLostCalled, is(false));
  }

  @Test
  public void afterReleasingContainer_onControlLostIsCalled() {
    /* Given */
    TestContainer container = mock(TestContainer.class);
    mPresenter.acquire(container);
    mPresenter.onControlGainedCalled = false;

    /* When */
    mPresenter.releaseContainer();

    /* Then */
    assertThat(mPresenter.onControlLostCalled, is(true));
    assertThat(mPresenter.onControlGainedCalled, is(false));
  }

  private static class TestPresenter extends Presenter<TestPresenter, TestContainer> {

    private boolean onControlGainedCalled;
    private boolean onControlLostCalled;

    @Override
    protected void onControlGained(@NotNull final TestContainer container) {
      onControlGainedCalled = true;
    }

    @Override
    protected void onControlLost() {
      onControlLostCalled = true;
    }
  }

  private static class TestContainer extends RelativeLayoutContainer<TestPresenter, TestContainer> {

    protected TestContainer(final Context context) {
      super(context);
    }

    protected TestContainer(final Context context, final AttributeSet attrs) {
      super(context, attrs);
    }

    protected TestContainer(final Context context, final AttributeSet attrs, final int defStyle) {
      super(context, attrs, defStyle);
    }
  }
}