package com.nhaarman.triad;

import android.content.Context;
import android.util.AttributeSet;

public class TestRelativeLayoutScreenContainer extends RelativeLayoutScreenContainer<ActivityComponent, TestPresenter, TestRelativeLayoutScreenContainer> {

  public TestRelativeLayoutScreenContainer(final Context context) {
    super(context);
  }

  public TestRelativeLayoutScreenContainer(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public TestRelativeLayoutScreenContainer(final Context context, final AttributeSet attrs, final int defStyle) {
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