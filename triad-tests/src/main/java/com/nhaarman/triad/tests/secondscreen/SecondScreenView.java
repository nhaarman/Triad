package com.nhaarman.triad.tests.secondscreen;

import android.content.Context;
import android.util.AttributeSet;
import com.nhaarman.triad.container.RelativeLayoutContainer;

public class SecondScreenView extends RelativeLayoutContainer<SecondScreenPresenter, SecondScreenContainer> implements SecondScreenContainer {

  public SecondScreenView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public SecondScreenView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  public SecondScreenView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }
}
