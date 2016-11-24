package com.nhaarman.triad;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface Triad {

    @NonNull
    Backstack getBackstack();

    boolean isTransitioning();

    void setListener(@Nullable Listener<?> listener);

    /**
     * Sets the current Activity, to be able to start other Activities from the Triad instance.
     */
    void setActivity(@Nullable Activity activity);

    /**
     * Initializes the backstack with given Screen. If the backstack is not empty, this call is ignored.
     * This method must be called before any other backstack operation.
     *
     * @param screen The Screen to start with.
     */
    void startWith(Screen<?> screen);

    /**
     * Initializes the backstack with given Screen. If the backstack is not empty, this call is ignored.
     * This method must be called before any other backstack operation.
     *
     * @param screen The Screen to start with.
     */
    void startWith(@NonNull Screen<?> screen, @Nullable TransitionAnimator animator);

    /**
     * Initializes with given backstack. If the current backstack is not empty, this call is ignored.
     * This method must be called before any other backstack operation.
     */
    void startWith(@NonNull Backstack backstack);

    /**
     * Pushes given Screen onto the backstack.
     *
     * One must first initialize this instance with [.startWith] before this method is called.
     *
     * @param screen The screen to push onto the backstack.
     */
    void goTo(@NonNull Screen<?> screen);

    /**
     * Pushes given Screen onto the backstack.
     *
     * One must first initialize this instance with [.startWith] before this method is called.
     *
     * @param screen The screen to push onto the backstack.
     */
    void goTo(@NonNull Screen<?> screen, @Nullable TransitionAnimator animator);

    /**
     * Forces a notification of the current screen.
     */
    void showCurrent();

    /**
     * Pops the backstack until given Screen is found.
     * If the Screen is not found, the Screen is pushed onto the current backstack.
     * Does nothing if the Screen is already on top of the stack.
     *
     * One must first initialize this instance with [.startWith] before this method is called.
     *
     * @param screen The Screen to pop to.
     */
    void popTo(@NonNull Screen<?> screen);

    /**
     * Pops the backstack until given Screen is found.
     * If the Screen is not found, the Screen is pushed onto the current backstack.
     * Does nothing if the Screen is already on top of the stack.
     *
     * One must first initialize this instance with [.startWith] before this method is called.
     *
     * @param screen The Screen to pop to.
     */
    void popTo(@NonNull Screen<?> screen, @Nullable TransitionAnimator animator);

    /**
     * Replaces the current Screen with given Screen.
     *
     * One must first initialize this instance with [.startWith] before this method is called.
     *
     * @param screen The Screen to replace the current Screen.
     */
    void replaceWith(@NonNull Screen<?> screen);

    /**
     * Replaces the current Screen with given Screen.
     *
     * One must first initialize this instance with [.startWith] before this method is called.
     *
     * @param screen The Screen to replace the current Screen.
     */
    void replaceWith(@NonNull Screen<?> screen, @Nullable TransitionAnimator animator);

    /**
     * Pops the current screen off the backstack.
     * Does nothing if the backstack would be empty afterwards.
     *
     * One must first initialize this instance with [.startWith] before this method is called.
     *
     * @return true if the transition will execute.
     */
    boolean goBack();

    /**
     * Replaces the entire backstack with given backstack, in a forward manner.
     *
     * One must first initialize this instance with [.startWith] before this method is called.
     *
     * @param newBackstack The new backstack.
     */
    void forward(@NonNull Backstack newBackstack);

    /**
     * Replaces the entire backstack with given backstack, in a backward manner.
     *
     * One must first initialize this instance with [.startWith] before this method is called.
     *
     * @param newBackstack The new backstack.
     */
    void backward(@NonNull Backstack newBackstack);

    /**
     * Replaces the entire backstack with given backstack, in a replace manner.
     *
     * One must first initialize this instance with [.startWith] before this method is called.
     *
     * @param newBackstack The new backstack.
     */
    void replace(@NonNull Backstack newBackstack);

    /**
     * Replaces the entire backstack with given backstack, in a replace manner.
     *
     * One must first initialize this instance with [.startWith] before this method is called.
     *
     * @param newBackstack The new backstack.
     */
    void replace(@NonNull Backstack newBackstack, @Nullable TransitionAnimator animator);

    /**
     * Returns whether the given Intent can be launched.
     */
    boolean canStart(@NonNull Intent intent);

    /**
     * Launches the Activity described by given Intent.
     *
     * @param intent The Activity to start.
     */
    void startActivity(@NonNull Intent intent);

    /**
     * Launches the Activity described by given Intent for result.
     *
     * @param intent   The Activity to start for result.
     * @param listener The callback to notify for the result.
     */
    void startActivityForResult(@NonNull Intent intent, @NonNull ActivityResultListener listener);

    /**
     * Propagates the activity result to the proper [ActivityResultListener].
     *
     * @param requestCode The request code, as created by [.startActivityForResult].
     * @param resultCode  The result code of the returning Activity.
     * @param data        The result data of the returning Activity.
     */
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    interface Listener<T> {

        void screenPushed(@NonNull Screen<T> pushedScreen);

        void screenPopped(@NonNull Screen<T> poppedScreen);

        /**
         * Notifies the listener that the backstack will forward to a new Screen.
         *
         * @param newScreen The new Screen to be shown.
         * @param callback  Must be called to indicate completion.
         */
        void forward(@NonNull Screen<T> newScreen, @Nullable TransitionAnimator animator, @NonNull Callback callback);

        /**
         * Notifies the listener that the backstack will be moved back to given Screen.
         *
         * @param newScreen The new screen to be shown.
         * @param callback  Must be called to indicate completion.
         */
        void backward(@NonNull Screen<T> newScreen, @Nullable TransitionAnimator animator, @NonNull Callback callback);

        /**
         * Notifies the listener that the backstack will be replaced, with given Screen on top.
         *
         * @param newScreen The new screen to be shown.
         * @param callback  Must be called to indicate completion.
         */
        void replace(@NonNull Screen<T> newScreen, @Nullable TransitionAnimator animator, @NonNull Callback callback);
    }

    /**
     * Supplied by Triad to the Listener, which is responsible for calling onComplete().
     */
    interface Callback {

        /**
         * Must be called exactly once to indicate that the corresponding transition has completed.
         * <p>
         * If not called, the backstack will not be updated and further calls to Triad will not execute.
         * Calling more than once will result in an exception.
         */
        void onComplete();
    }

    interface ActivityResultListener {

        void onActivityResult(int resultCode, @Nullable Intent data);
    }
}