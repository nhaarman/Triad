package com.nhaarman.triad;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.test.internal.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.test.ActivityInstrumentationTestCase2;
import android.view.ViewGroup;
import com.nhaarman.triad.tests.R;
import com.nhaarman.triad.tests.TestActivity;
import flow.Flow;
import java.util.Collection;

import static com.nhaarman.triad.utils.ViewWaiter.viewVisible;
import static com.nhaarman.triad.utils.ViewWaiter.waitUntil;

public abstract class TestActivityInstrumentationTestCase extends ActivityInstrumentationTestCase2<TestActivity> {

  protected TestActivity mActivity;

  protected ViewGroup mScreenHolder;

  protected Flow mFlow;

  protected TestActivityInstrumentationTestCase() {
    super(TestActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    mScreenHolder = (ViewGroup) getActivity().findViewById(R.id.view_triad_screenholder);

    mFlow = getActivity().getFlow();

    getInstrumentation().waitForIdleSync();
    waitUntil(viewVisible(mScreenHolder, R.id.view_screen_first));
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    FlowManager.destroyInstance();
  }

  protected void rotate() {
    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    getActivity();

    mScreenHolder = (ViewGroup) getActivity().findViewById(R.id.view_triad_screenholder);

    mFlow = getActivity().getFlow();
  }

  @Override
  public TestActivity getActivity() {
    getInstrumentation().runOnMainSync(new Runnable() {
      @Override
      public void run() {
        Collection<Activity> activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
        if (activities.isEmpty()) {
          mActivity = null;
        } else {
          mActivity = (TestActivity) activities.iterator().next();
        }
      }
    });
    getInstrumentation().waitForIdleSync();

    return mActivity == null ? super.getActivity() : mActivity;
  }
}
