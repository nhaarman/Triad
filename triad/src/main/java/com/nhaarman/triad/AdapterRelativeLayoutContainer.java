package com.nhaarman.triad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;

import static com.nhaarman.triad.Preconditions.checkState;
import static com.nhaarman.triad.TriadUtil.findActivityComponent;

/**
 * An abstract RelativeLayout {@link Container} instance that handles {@link Presenter} management
 * for use in an {@link AdapterView},
 * and uses Butter Knife to bind view fields in implementing classes.
 *
 * @param <P> The specialized {@link Presenter} type.
 */
public abstract class AdapterRelativeLayoutContainer<
    P extends Presenter<?, ActivityComponent>,
    ActivityComponent
    > extends RelativeLayout implements AdapterContainer<P> {

  @NonNull
  private final ActivityComponent mActivityComponent;

  /* Use a raw type in favor of an easier API. */
  @SuppressWarnings("rawtypes")
  @Nullable
  private Presenter mPresenter;

  private boolean mIsAttachedToWindow;

  public AdapterRelativeLayoutContainer(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public AdapterRelativeLayoutContainer(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);

    mActivityComponent = findActivityComponent(context);
  }

  /**
   * Returns the {@link P} instance that is tied to this {@code RelativeLayoutContainer}.
   */
  @NonNull
  public P getPresenter() {
    checkState(mPresenter != null, "Presenter is null");

    return (P) mPresenter;
  }

  @Override
  public void setPresenter(@NonNull final P presenter) {
    if (mPresenter != null) {
      mPresenter.releaseContainer();
    }

    mPresenter = presenter;

    if (mIsAttachedToWindow) {
      mPresenter.acquire(this, mActivityComponent);
    }
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    ButterKnife.bind(this);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    if (mPresenter != null) {
      mPresenter.acquire(this, mActivityComponent);
    }

    mIsAttachedToWindow = true;
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();

    if (mPresenter != null) {
      mPresenter.releaseContainer();
    }

    mIsAttachedToWindow = false;
  }
}