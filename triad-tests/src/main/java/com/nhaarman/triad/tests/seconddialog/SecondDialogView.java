package com.nhaarman.triad.tests.seconddialog;

import android.content.Context;
import android.util.AttributeSet;
import com.nhaarman.triad.container.RelativeLayoutContainer;

public class SecondDialogView extends RelativeLayoutContainer<SecondDialogPresenter, SecondDialogContainer> implements SecondDialogContainer{

  public SecondDialogView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public SecondDialogView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  public SecondDialogView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }
}
