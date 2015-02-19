package com.nhaarman.gable.container;

import android.content.Context;
import android.util.AttributeSet;
import com.nhaarman.gable.presenter.Presenter;
import java.util.Observable;
import java.util.Observer;
import org.jetbrains.annotations.NotNull;

public abstract class ViewModelRelativeLayoutContainer<P extends Presenter<P, C>, C extends Container<P, C>, M extends Observable>
    extends RelativeLayoutContainer<P, C>
    implements ViewModelContainer<M>,
               Observer {

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
  }

  @Override
  public final void update(final Observable observable, final Object data) {
    if (observable == mViewModel) {
      updateViewModel(mViewModel, data);
    }
  }

  protected abstract void updateViewModel(@NotNull final M viewModel, final Object data);
}