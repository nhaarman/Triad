package com.nhaarman.triad.tests.firstdialog;

import android.content.Context;
import android.util.AttributeSet;
import com.nhaarman.triad.container.RelativeLayoutContainer;

public class FirstDialogView extends RelativeLayoutContainer<FirstDialogPresenter, FirstDialogContainer> implements FirstDialogContainer{

  public FirstDialogView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public FirstDialogView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  public FirstDialogView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }
}
