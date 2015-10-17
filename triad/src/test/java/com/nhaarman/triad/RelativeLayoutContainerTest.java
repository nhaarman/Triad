package com.nhaarman.triad;

import android.app.Activity;
import android.app.Application;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class RelativeLayoutContainerTest {

  private TestRelativeLayoutContainer mRelativeLayoutContainer;

  private Presenter<ActivityComponent, TestRelativeLayoutContainer> mPresenterMock;

  private ActivityComponent mActivityComponentMock;

  @Before
  public void setUp() {
    Activity activity = mock(Activity.class, withSettings().extraInterfaces(ActivityComponentProvider.class, ScreenProvider.class));
    Application application = mock(Application.class, withSettings().extraInterfaces(TriadProvider.class));
    when(activity.getApplicationContext()).thenReturn(application);

    Screen<ApplicationComponent> screen = mock(Screen.class);
    mPresenterMock = mock(Presenter.class);
    when(screen.getPresenter(any(Class.class))).thenReturn(mPresenterMock);

    when(((ScreenProvider<ApplicationComponent>) activity).getCurrentScreen()).thenReturn(screen);
    mActivityComponentMock = mock(ActivityComponent.class);
    when(((ActivityComponentProvider<ActivityComponent>) activity).getActivityComponent()).thenReturn(mActivityComponentMock);

    mRelativeLayoutContainer = new TestRelativeLayoutContainer(activity, null, 0);
  }

  @Test
  public void getPresenter_returnsProperPresenter() {
    /* Then */
    assertThat(mRelativeLayoutContainer.getPresenter(), is(mPresenterMock));
  }

  @Test
  public void onAttachedToWindow_givesControlToPresenter() {
    /* When */
    mRelativeLayoutContainer.onAttachedToWindow();

    /* Then */
    verify(mPresenterMock).acquire(mRelativeLayoutContainer, mActivityComponentMock);
  }

  @Test
  public void onDetachedFromWindow_releasesPresenterControl() {
    /* When */
    mRelativeLayoutContainer.onDetachedFromWindow();

    /* Then */
    verify(mPresenterMock).releaseContainer();
  }
}