package com.nhaarman.gable.container;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import com.nhaarman.gable.presenter.Presenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class RelativeLayoutContainer<P extends Presenter<P, C>, C extends Container<P, C>>
    extends RelativeLayout
    implements Container<P, C> {

  @Nullable
  private P mPresenter;

  protected RelativeLayoutContainer(final Context context) {
    super(context);
  }

  protected RelativeLayoutContainer(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  protected RelativeLayoutContainer(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  @NotNull
  public P getPresenter() {
    if (mPresenter == null) {
      throw new NullPointerException("Presenter has not been set.");
    }

    return mPresenter;
  }

  @Override
  public void setPresenter(@NotNull final P presenter) {
    mPresenter = presenter;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    if (isInEditMode()) {
      return;
    }

    ButterKnife.inject(this);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (isInEditMode()) {
      return;
    }

    getPresenter().acquire((C) this);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    getPresenter().releaseContainer();
  }
}
