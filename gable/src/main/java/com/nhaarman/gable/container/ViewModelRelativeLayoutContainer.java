package com.nhaarman.gable.container;

import android.content.Context;
import android.util.AttributeSet;
import com.nhaarman.gable.presenter.Presenter;
import java.util.Observable;
import java.util.Observer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ViewModelRelativeLayoutContainer<P extends Presenter<P, C>, C extends Container<P, C>, M extends Observable>
    extends RelativeLayoutContainer<P, C>
    implements ViewModelContainer<M>,
               Observer {

  @Nullable
  private M mViewModel;

  protected ViewModelRelativeLayoutContainer(final Context context) {
    super(context);
  }

  protected ViewModelRelativeLayoutContainer(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  protected ViewModelRelativeLayoutContainer(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public void setViewModel(@NotNull final M viewModel) {
    mViewModel = viewModel;
    mViewModel.addObserver(this);
    update(mViewModel, null);
  }

  @Override
  public final void update(final Observable observable, final Object data) {
    //noinspection ObjectEquality
    if (mViewModel != null && observable == mViewModel) {
      updateViewModel(mViewModel, data);
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (mViewModel != null) {
      mViewModel.deleteObserver(this);
    }
  }

  protected abstract void updateViewModel(@NotNull final M m, final Object data);
}
