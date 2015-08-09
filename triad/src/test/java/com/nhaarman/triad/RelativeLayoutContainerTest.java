package com.nhaarman.triad;

import android.content.Context;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RelativeLayoutContainerTest {

  private TestRelativeLayoutContainer mRelativeLayoutContainer;

  @Before
  public void setUp() {
    Context context = mock(Context.class);
    mRelativeLayoutContainer = new TestRelativeLayoutContainer(context);
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
    mRelativeLayoutContainer.setPresenterAndActivityComponent(presenter, new ActivityComponent());

    /* Then */
    assertThat(mRelativeLayoutContainer.getPresenter(), is(presenter));
  }

  @Test
  public void onAttachedToWindow_givesControlToPresenter() {
    /* Given */
    TestPresenter presenter = mock(TestPresenter.class);
    ActivityComponent activityComponent = mock(ActivityComponent.class);
    mRelativeLayoutContainer.setPresenterAndActivityComponent(presenter, activityComponent);

    /* When */
    mRelativeLayoutContainer.onAttachedToWindow();

    /* Then */
    verify(presenter).acquire(mRelativeLayoutContainer, activityComponent);
  }

  @Test
  public void onDetachedFromWindow_releasesPresenterControl() {
    /* Given */
    TestPresenter presenter = mock(TestPresenter.class);
    mRelativeLayoutContainer.setPresenterAndActivityComponent(presenter, new ActivityComponent());
    mRelativeLayoutContainer.onAttachedToWindow();

    /* When */
    mRelativeLayoutContainer.onDetachedFromWindow();

    /* Then */
    verify(presenter).releaseContainer();
  }
}