/*
 * Copyright 2015 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.triad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ListView;
import butterknife.ButterKnife;

import static com.nhaarman.triad.TriadUtil.findActivityComponent;
import static com.nhaarman.triad.TriadUtil.findPresenter;

/**
 * An abstract ListView {@link Container} instance that handles {@link Presenter} management,
 * and uses Butter Knife to bind view fields in implementing classes.
 *
 * @param <P> The specialized {@link Presenter} type.
 * @param <C> The specialized {@link Container} type.
 */
public abstract class ListViewContainer<
    ActivityComponent,
    P extends Presenter<ActivityComponent, C>,
    C extends Container
    > extends ListView implements Container {

  @NonNull
  private final P mPresenter;

  @NonNull
  private final ActivityComponent mActivityComponent;

  public ListViewContainer(final Context context, final AttributeSet attrs, final Class<P> presenterClass) {
    this(context, attrs, 0, presenterClass);
  }

  public ListViewContainer(final Context context, final AttributeSet attrs, final int defStyle, final Class<P> presenterClass) {
    super(context, attrs, defStyle);

    mActivityComponent = findActivityComponent(context);
    mPresenter = findPresenter(context, presenterClass);
  }

  /**
   * Returns the {@link P} instance that is tied to this {@code RelativeLayoutContainer}.
   */
  @NonNull
  public P getPresenter() {
    return mPresenter;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    ButterKnife.bind(this);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    if (isInEditMode()) {
      return;
    }

    mPresenter.acquire((C) this, mActivityComponent);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();

    mPresenter.releaseContainer();
  }
}
