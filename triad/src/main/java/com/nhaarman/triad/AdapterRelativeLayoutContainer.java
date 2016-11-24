/*
 * Copyright 2016 Niek Haarman
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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import static com.nhaarman.triad.Preconditions.checkState;
import static com.nhaarman.triad.TriadUtil.findActivityComponent;

/**
 * An abstract RelativeLayout {@link Container} instance that handles {@link Presenter} management
 * for use in an adapter View.
 *
 * @param <P> The specialized {@link Presenter} type.
 */
public abstract class AdapterRelativeLayoutContainer
      <P extends Presenter<?, ActivityComponent>, ActivityComponent>
      extends RelativeLayout implements AdapterContainer<P> {

    @NonNull
    private final ActivityComponent activityComponent;

    /* Use a raw type in favor of an easier API. */
    @SuppressWarnings("rawtypes")
    @Nullable
    private Presenter presenter;

    private boolean isAttachedToWindow;

    public AdapterRelativeLayoutContainer(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdapterRelativeLayoutContainer(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        activityComponent = findActivityComponent(context, this);
    }

    /**
     * Returns the {@link P} instance that is tied to this {@code RelativeLayoutContainer}.
     */
    @NonNull
    public P getPresenter() {
        checkState(presenter != null, "Presenter is null");

        return (P) presenter;
    }

    @Override
    public void setPresenter(@NonNull final P presenter) {
        if (this.presenter != null) {
            this.presenter.releaseContainer(this);
        }

        this.presenter = presenter;

        if (isAttachedToWindow) {
            this.presenter.acquire(this, activityComponent);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (presenter != null) {
            presenter.acquire(this, activityComponent);
        }

        isAttachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (presenter != null) {
            presenter.releaseContainer(this);
        }

        isAttachedToWindow = false;
    }
}
