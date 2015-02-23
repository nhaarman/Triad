package com.nhaarman.triad.sample.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import com.nhaarman.triad.container.RelativeLayoutContainer;
import com.nhaarman.triad.sample.R;
import java.util.Observable;
import java.util.Observer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MainView extends RelativeLayoutContainer<MainPresenter, MainContainer> implements MainContainer, Observer {

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

  public MainView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  public void setViewModel(@NotNull final MainViewModel viewModel) {
    viewModel.addObserver(this);
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

  @Optional
  @OnClick(R.id.view_main_newscreenbutton)
  public void onNewScreenButtonClicked() {
    getPresenter().onNewScreenButtonClicked();
  }

  @Override
  public void update(final Observable observable, final Object data) {
    MainViewModel mainViewModel = (MainViewModel) observable;

    mCounterTV.setText(mainViewModel.getCounterText());

    if (mSecondCounterTV != null) {
      mSecondCounterTV.setText(mainViewModel.getSecondCounterText());
    }
  }
}
