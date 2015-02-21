package com.nhaarman.triad.tests.firstscreen;

import android.content.Context;
import android.util.AttributeSet;
import com.nhaarman.triad.container.RelativeLayoutContainer;

public class FirstScreenView extends RelativeLayoutContainer<FirstScreenPresenter, FirstScreenContainer> implements FirstScreenContainer {

  public FirstScreenView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public FirstScreenView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  public FirstScreenView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }
}
