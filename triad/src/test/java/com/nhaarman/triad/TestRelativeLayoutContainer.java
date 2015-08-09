package com.nhaarman.triad;

import android.content.Context;
import android.util.AttributeSet;

public class TestRelativeLayoutContainer extends RelativeLayoutContainer<ActivityComponent, TestPresenter, TestRelativeLayoutContainer> {

  public TestRelativeLayoutContainer(final Context context) {
    super(context);
  }

  public TestRelativeLayoutContainer(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public TestRelativeLayoutContainer(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
  }

  @Override
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
  }
}