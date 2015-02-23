package com.nhaarman.triad.sample.main.maindialog;

import android.content.Context;
import android.util.AttributeSet;
import butterknife.OnClick;
import com.nhaarman.triad.container.RelativeLayoutContainer;
import com.nhaarman.triad.sample.R;

public class MainDialogView extends RelativeLayoutContainer<MainDialogPresenter, MainDialogContainer> implements MainDialogContainer {

  public MainDialogView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public MainDialogView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  public MainDialogView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @OnClick(R.id.view_main_dialog_button)
  public void onButtonClicked() {
    getPresenter().onButtonClicked();
  }
}
