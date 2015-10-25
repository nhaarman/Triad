package com.nhaarman.triad;

import android.content.Context;
import android.util.AttributeSet;

public class TestRelativeLayoutContainer extends RelativeLayoutContainer<TestPresenter, ActivityComponent> {

  public TestRelativeLayoutContainer(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle, TestPresenter.class);
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