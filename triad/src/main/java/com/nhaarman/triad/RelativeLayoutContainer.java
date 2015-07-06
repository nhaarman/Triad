/*
 * Copyright 2015 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.triad;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An abstract {@link Container} instance that handles {@link Presenter} management,
 * and uses Butter Knife to inject view fields in implementing classes.
 *
 * @param <P> The specialized {@link Presenter} type.
 * @param <C> The specialized {@link Container} type.
 */
public abstract class RelativeLayoutContainer<P extends Presenter<P, C>, C extends Container<P, C>>
    extends RelativeLayout
    implements Container<P, C> {

  /**
   * The {@link P} that is tied to this instance.
   */
  @Nullable
  private P mPresenter;

  public RelativeLayoutContainer(final Context context) {
    super(context);
  }

  public RelativeLayoutContainer(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public RelativeLayoutContainer(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  @TargetApi(21)
  public RelativeLayoutContainer(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  /**
   * Returns the {@link P} instance that is tied to this {@code RelativeLayoutContainer}.
   */
  @NotNull
  public P getPresenter() {
    if (mPresenter == null) {
      throw new NullPointerException("Presenter has not been set.");
    }

    return mPresenter;
  }

  /**
   * Sets the {@link P} that controls this {@code RelativeLayoutContainer}.
   *
   * @param presenter The {@link P} instance.
   */
  @Override
  public final void setPresenter(@NotNull final P presenter) {
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