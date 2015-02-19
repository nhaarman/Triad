package com.nhaarman.sample.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import com.nhaarman.gable.container.ViewModelRelativeLayoutContainer;
import com.nhaarman.sample.R;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MainView extends ViewModelRelativeLayoutContainer<MainPresenter, MainContainer, MainViewModel> implements MainContainer {

  @NotNull
  @InjectView(R.id.view_main_countertv)
  protected TextView mCounterTV;

  @Nullable
  @Optional
  @InjectView(R.id.view_main_secondcountertv)
  protected TextView mSecondCounterTV;

  public MainView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public MainView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @OnClick(R.id.view_main_incrementbutton)
  public void onIncrementButtonClicked() {
    getPresenter().onIncrementButtonClicked();
  }

  @Optional
  @OnClick(R.id.view_main_secondincrementbutton)
  public void onSecondIncrementButtonClicked() {
    getPresenter().onSecondIncrementButtonClicked();
  }

  @Override
  protected void updateViewModel(@NotNull final MainViewModel mainViewModel, final Object data) {
    mCounterTV.setText(mainViewModel.getCounterText());

    if (mSecondCounterTV != null) {
      mSecondCounterTV.setText(mainViewModel.getSecondCounterText());
    }
  }
}
