package com.nhaarman.triad;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;
import com.nhaarman.triad.tests.R;
import com.nhaarman.triad.tests.TestActivity;
import flow.Flow;

import static com.nhaarman.triad.tests.utils.ViewWaiter.viewVisible;
import static com.nhaarman.triad.tests.utils.ViewWaiter.waitUntil;

public abstract class TestActivityInstrumentationTestCase extends ActivityInstrumentationTestCase2<TestActivity> {

  protected ViewGroup mScreenHolder;
  protected View mDimmerView;
  protected ViewGroup mDialogHolder;
  protected Flow mFlow;

  protected TestActivityInstrumentationTestCase() {
    super(TestActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    mScreenHolder = (ViewGroup) getActivity().findViewById(R.id.view_triad_screenholder);
    mDimmerView = getActivity().findViewById(R.id.view_triad_dimmerview);
    mDialogHolder = (ViewGroup) getActivity().findViewById(R.id.view_triad_dialogholder);

    mFlow = getActivity().getFlow();

    getInstrumentation().waitForIdleSync();
    waitUntil(viewVisible(mScreenHolder, R.id.view_screen_first));
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    FlowManager.destroyInstance();
  }
}
