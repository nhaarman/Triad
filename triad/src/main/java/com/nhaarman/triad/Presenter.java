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

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

/**
 * The Presenter class in the MVP context.
 *
 * In the Model-View-Presenter pattern, the {@code Presenter} retrieves data from the
 * {@code Model}, and formats it so the View ({@link Container}) can present it.
 *
 * The lifecycle of a Presenter consists of two methods:
 * <li>
 * <ul>{@link #acquire(Container, Object)}</ul>
 * <ul>{@link #releaseContainer()}</ul>
 * </li>
 *
 * Control over the {@link Container} instance starts at {@link #acquire(Container, Object)},
 * and ends at {@link #releaseContainer()}.
 * There are no guarantees on the order of calls to these two methods.
 *
 * Presenters survive configuration changes, making it easy to manage data and
 * asynchronous calls.
 *
 * @param <C>                 The {@link Container} instance this {@code Presenter} controls.
 * @param <ActivityComponent> The {@code ActivityComponent}.
 */
public interface Presenter<C extends Container, ActivityComponent> {

    /**
     * Binds the {@link Container} this {@code BasePresenter} controls.
     * From this point on, the {@code Presenter} may manipulate given {@link Container} instance,
     * until {@link #releaseContainer()} is called.
     *
     * @param container The {@link Container} to gain control over.
     */
    @MainThread
    void acquire(@NonNull C container, @NonNull ActivityComponent activityComponent);

    /**
     * Tells this Presenter to release the {@link Container} instance.
     * From this point on, the {@code Presenter} is not allowed to manipulate
     * the {@link Container} instance supplied to {@link #acquire(Container, Object)} anymore.
     */
    @MainThread
    void releaseContainer();
}
