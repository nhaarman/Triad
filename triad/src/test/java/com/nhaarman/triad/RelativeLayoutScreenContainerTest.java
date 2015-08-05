package com.nhaarman.triad;

import android.content.Context;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RelativeLayoutScreenContainerTest {

  private TestRelativeLayoutScreenContainer mRelativeLayoutContainer;

  @Before
  public void setUp() {
    Context context = mock(Context.class);
    mRelativeLayoutContainer = new TestRelativeLayoutScreenContainer(context);
  }

  @Test(expected = NullPointerException.class)
  public void initially_getPresenterThrowsANullPointerException() {
    mRelativeLayoutContainer.getPresenter();
  }

  @Test
  public void afterSettingPresenter_getPresenterReturnsThatPresenter() {
    /* Given */
    TestPresenter presenter = mock(TestPresenter.class);

    /* When */
    mRelativeLayoutContainer.setPresenter(presenter);

    /* Then */
    assertThat(mRelativeLayoutContainer.getPresenter(), is(presenter));
  }

  @Test
  public void onAttachedToWindow_givesControlToPresenter() {
    /* Given */
    TestPresenter presenter = mock(TestPresenter.class);
    ActivityComponent activityComponent = mock(ActivityComponent.class);
    mRelativeLayoutContainer.setPresenter(presenter);
    mRelativeLayoutContainer.setActivityComponent(activityComponent);

    /* When */
    mRelativeLayoutContainer.onAttachedToWindow();

    /* Then */
    verify(presenter).acquire(mRelativeLayoutContainer, activityComponent);
  }

  @Test
  public void onDetachedFromWindow_releasesPresenterControl() {
    /* Given */
    TestPresenter presenter = mock(TestPresenter.class);
    mRelativeLayoutContainer.setPresenter(presenter);
    mRelativeLayoutContainer.setActivityComponent(mock(ActivityComponent.class));
    mRelativeLayoutContainer.onAttachedToWindow();

    /* When */
    mRelativeLayoutContainer.onDetachedFromWindow();

    /* Then */
    verify(presenter).releaseContainer();
  }
}