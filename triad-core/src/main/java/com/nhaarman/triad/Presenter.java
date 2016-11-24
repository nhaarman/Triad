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
 * <p>
 * {@link #acquire(Container, Object)}
 * {@link #releaseContainer(Container)}
 * <p>
 *
 * Control over the {@link Container} instance starts at {@link #acquire(Container, Object)},
 * and ends at {@link #releaseContainer(Container)}.
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
     * until {@link #releaseContainer(Container)} is called.
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
    void releaseContainer(@NonNull C container);
}
