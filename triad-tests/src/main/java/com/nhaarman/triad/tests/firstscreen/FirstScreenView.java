package com.nhaarman.triad.tests.firstscreen;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import butterknife.OnClick;
import com.nhaarman.triad.RelativeLayoutContainer;

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

  @OnClick(com.nhaarman.triad.tests.R.id.view_screen_first_button)
  public void onButtonClicked() {
    getPresenter().onButtonClicked();
  }

  @Override
  public void setText(final String text) {
    ((TextView) findViewById(com.nhaarman.triad.tests.R.id.view_screen_first_tv)).setText(text);
  }
}
