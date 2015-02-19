package com.nhaarman.gable.screen;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.nhaarman.gable.container.ScreenContainer;
import com.nhaarman.gable.presenter.ScreenPresenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Screen<P extends ScreenPresenter<P, C>, C extends ScreenContainer<P, C>, M> {

  @Nullable
  private P mPresenter;

  protected abstract int getLayoutResId();

  @NotNull
  protected abstract P createPresenter(@NotNull final M m);

  @NotNull
  public final C createView(@NotNull final ViewGroup parent) {
    return (C) LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(), parent, false);
  }

  @NotNull
  public final P getPresenter(@NotNull final M component) {
    if (mPresenter == null) {
      mPresenter = createPresenter(component);
    }
    return mPresenter;
  }
}
