package com.nhaarman.gable.presenter;

import com.nhaarman.gable.container.Container;
import com.nhaarman.gable.container.ViewModelContainer;
import java.util.Observable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ViewModelScreenPresenter<P extends Presenter<P, C>, C extends Container<P, C> & ViewModelContainer<M>, M extends Observable> extends ScreenPresenter<P, C> {

  @Nullable
  private M mViewModel;

  @Override
  protected void onControlGained(@NotNull final C container) {
    super.onControlGained(container);
    if (mViewModel == null) {
      mViewModel = createViewModel();
    }

    container.setViewModel(mViewModel);
  }

  @NotNull
  protected abstract M createViewModel();

  @NotNull
  protected M getViewModel() {
    if (mViewModel == null) {
      throw new AssertionError(); // TODO: Find out in which scenarios this happens.
    }
    return mViewModel;
  }
}
