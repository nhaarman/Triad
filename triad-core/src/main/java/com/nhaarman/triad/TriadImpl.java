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

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

import static com.nhaarman.triad.Preconditions.checkNotNull;
import static com.nhaarman.triad.Preconditions.checkState;

/**
 * Holds the current truth, the history of screens, and exposes operations to change it.
 */
class TriadImpl implements Triad {

    @NonNull
    private final SparseArray<ActivityResultListener> activityResultListeners;

    @SuppressWarnings("rawtypes")
    @Nullable
    private Listener listener;

    @NonNull
    private Backstack backstack;

    @Nullable
    private Transition transition;

    @NonNull
    private WeakReference<Activity> activity;

    private int requestCodeCounter;

    private TriadImpl(@NonNull final Backstack backstack) {
        this(backstack, null);
    }

    private TriadImpl(@NonNull final Backstack backstack, @Nullable final Listener<?> listener) {
        this.listener = listener;
        this.backstack = backstack;

        activityResultListeners = new SparseArray<>();

        activity = new WeakReference<>(null);
    }

    /**
     * Sets the current Activity, to be able to start other Activities from the Triad instance.
     */
    @Override
    public void setActivity(@Nullable final Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public void setListener(@Nullable final Listener<?> listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    public Backstack getBackstack() {
        return backstack;
    }

    @Override
    public boolean isTransitioning() {
        return transition != null && !transition.isFinished();
    }

    /**
     * Initializes the backstack with given Screen. If the backstack is not empty, this call is ignored.
     * This method must be called before any other backstack operation.
     *
     * @param screen The Screen to start with.
     */
    @Override
    public void startWith(@NonNull final Screen<?> screen) {
        startWith(screen, null);
    }

    @Override
    public void startWith(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
        if (backstack.size() == 0 && transition == null) {
            move(new StartWithTransition(screen, animator));
        }
    }

    @Override
    public void startWith(@NonNull final Backstack backstack) {
        if (this.backstack.size() == 0 && transition == null) {
            move(new StartWithBackstackTransition(backstack));
        }
    }

    /**
     * Pushes given Screen onto the backstack.
     *
     * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
     *
     * @param screen The screen to push onto the backstack.
     */
    @Override
    public void goTo(@NonNull final Screen<?> screen) {
        goTo(screen, null);
    }

    /**
     * Pushes given Screen onto the backstack.
     *
     * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
     *
     * @param screen The screen to push onto the backstack.
     */
    @Override
    public void goTo(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
        checkState(backstack.size() > 0 || transition != null, "Use startWith(Screen) to show your first Screen.");

        move(new GoToTransition(screen, animator));
    }

    /**
     * Forces a notification of the current screen.
     */
    @Override
    public void showCurrent() {
        if (transition != null && !transition.isFinished()) {
            transition.cancel();
            move(transition.copy());
        } else {
            move(new ShowTransition());
        }
    }

    /**
     * Pops the backstack until given Screen is found.
     * If the Screen is not found, the Screen is pushed onto the current backstack.
     * Does nothing if the Screen is already on top of the stack.
     *
     * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
     *
     * @param screen The Screen to pop to.
     */
    @Override
    public void popTo(@NonNull final Screen<?> screen) {
        popTo(screen, null);
    }

    /**
     * Pops the backstack until given Screen is found.
     * If the Screen is not found, the Screen is pushed onto the current backstack.
     * Does nothing if the Screen is already on top of the stack.
     *
     * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
     *
     * @param screen The Screen to pop to.
     */
    @Override
    public void popTo(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
        checkState(backstack.size() > 0 || transition != null, "Use startWith(Screen) to show your first Screen.");

        move(new PopToTransition(screen, animator));
    }

    /**
     * Replaces the current Screen with given Screen.
     *
     * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
     *
     * @param screen The Screen to replace the current Screen.
     */
    @Override
    public void replaceWith(@NonNull final Screen<?> screen) {
        replaceWith(screen, null);
    }

    /**
     * Replaces the current Screen with given Screen.
     *
     * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
     *
     * @param screen The Screen to replace the current Screen.
     */
    @Override
    public void replaceWith(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
        checkState(backstack.size() > 0 || transition != null, "Use startWith(Screen) to show your first Screen.");

        move(new ReplaceWithTransition(screen, animator));
    }

    /**
     * Pops the current screen off the backstack.
     * Does nothing if the backstack would be empty afterwards.
     *
     * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
     *
     * @return true if the transition will execute.
     */
    @Override
    public boolean goBack() {
        checkState(backstack.size() > 0 || transition != null, "Use startWith(Screen) to show your first Screen.");

        boolean canGoBack = backstack.size() > 1 || transition != null && !transition.isFinished();
        move(new GoBackTransition());
        return canGoBack;
    }

    /**
     * Replaces the entire backstack with given backstack, in a forward manner.
     *
     * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
     *
     * @param newBackstack The new backstack.
     */
    @Override
    public void forward(@NonNull final Backstack newBackstack) {
        checkState(backstack.size() > 0 || transition != null, "Use startWith(Screen) to show your first Screen.");

        move(new ForwardTransition(newBackstack));
    }

    /**
     * Replaces the entire backstack with given backstack, in a backward manner.
     *
     * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
     *
     * @param newBackstack The new backstack.
     */
    @Override
    public void backward(@NonNull final Backstack newBackstack) {
        checkState(backstack.size() > 0 || transition != null, "Use startWith(Screen) to show your first Screen.");

        move(new BackwardTransition(newBackstack));
    }

    /**
     * Replaces the entire backstack with given backstack, in a replace manner.
     *
     * One must first initialize this instance with {@link #startWith(Screen)} before this method is called.
     *
     * @param newBackstack The new backstack.
     */
    @Override
    public void replace(@NonNull final Backstack newBackstack) {
        checkState(backstack.size() > 0 || transition != null, "Use startWith(Screen) to show your first Screen.");

        move(new ReplaceTransition(newBackstack));
    }

    @Override
    public boolean canStart(@NonNull final Intent intent) {
        Activity activity = this.activity.get();
        if (activity != null) {
            List<ResolveInfo> infoList = activity.getPackageManager().queryIntentActivities(intent, 0);
            return !infoList.isEmpty();
        }

        return false;
    }

    /**
     * Launches the Activity described by given Intent.
     *
     * @param intent The Activity to start.
     */
    @Override
    public void startActivity(@NonNull final Intent intent) {
        checkState(activity.get() != null, "Activity reference is null.");

        activity.get().startActivity(intent);
    }

    /**
     * Launches the Activity described by given Intent for result.
     *
     * @param intent   The Activity to start for result.
     * @param listener The callback to notify for the result.
     */
    @Override
    public void startActivityForResult(@NonNull final Intent intent, @NonNull final ActivityResultListener listener) {
        checkState(activity.get() != null, "Activity reference is null.");

        activity.get().startActivityForResult(intent, requestCodeCounter);
        activityResultListeners.put(requestCodeCounter, listener);
        requestCodeCounter++;
    }

    /**
     * Propagates the activity result to the proper {@link ActivityResultListener}.
     *
     * @param requestCode The request code, as created by {@link #startActivityForResult(Intent, ActivityResultListener)}.
     * @param resultCode  The result code of the returning Activity.
     * @param data        The result data of the returning Activity.
     */
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        activityResultListeners.get(requestCode).onActivityResult(resultCode, data);
        activityResultListeners.remove(requestCode);
    }

    private void move(@NonNull final Transition transition) {
        if (this.transition == null || this.transition.isFinished() || this.transition.isCancelled()) {
            this.transition = transition;
            transition.execute();
        } else {
            this.transition.enqueue(transition);
        }
    }

    @NonNull
    public static Triad emptyInstance() {
        return new TriadImpl(Backstack.emptyBuilder().build());
    }

    @NonNull
    public static Triad newInstance(@NonNull final Backstack backstack, @NonNull final Listener<?> listener) {
        return new TriadImpl(backstack, listener);
    }

    private abstract class Transition implements Callback {

        private boolean finished;
        private boolean cancelled;

        @Nullable
        private Transition next;

        @Nullable
        private Backstack nextBackstack;

        void enqueue(@NonNull final Transition transition) {
            if (next == null) {
                next = transition;
            } else {
                next.enqueue(transition);
            }
        }

        protected void notifyScreenPopped(@NonNull final Screen<?> screen) {
            if (cancelled) return;
            checkState(listener != null, "Listener is null. Be sure to call setListener(Listener).");

            listener.screenPopped(screen);
        }

        protected void notifyScreenPushed(@NonNull final Screen<?> screen) {
            if (cancelled) return;
            checkState(listener != null, "Listener is null. Be sure to call setListener(Listener).");

            listener.screenPushed(screen);
        }

        protected void notifyForward(@NonNull final Backstack nextBackstack) {
            if (cancelled) return;
            checkState(listener != null, "Listener is null. Be sure to call setListener(Listener).");

            this.nextBackstack = nextBackstack;
            listener.forward(nextBackstack.current().screen, nextBackstack.current().animator, this);
        }

        protected void notifyBackward(@NonNull final Backstack nextBackstack, @Nullable final TransitionAnimator animator) {
            if (cancelled) return;
            checkState(listener != null, "Listener is null. Be sure to call setListener(Listener).");

            this.nextBackstack = nextBackstack;
            listener.backward(nextBackstack.current().screen, animator, this);
        }

        protected void notifyReplace(@NonNull final Backstack nextBackstack) {
            if (cancelled) return;
            checkState(listener != null, "Listener is null. Be sure to call setListener(Listener).");

            this.nextBackstack = nextBackstack;
            listener.replace(nextBackstack.current().screen, nextBackstack.current().animator, this);
        }

        protected abstract void execute();

        @Override
        public void onComplete() {
            if (cancelled) return;

            checkState(!finished, "onComplete already called for this transition");

            if (nextBackstack != null) {
                backstack = nextBackstack;
            }

            finished = true;
            if (next != null) {
                transition = next;
                transition.execute();
            }
        }

        public boolean isFinished() {
            return finished;
        }

        public void cancel() {
            cancelled = true;
        }

        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "[finished=" + finished + "]";
        }

        public abstract Transition copy();
    }

    private class GoBackTransition extends Transition {

        @Override
        public void execute() {
            if (backstack.size() == 1) {
                // We are not calling the listener, so we must complete this noop transition ourselves.
                onComplete();
            } else {
                Backstack.Builder builder = backstack.buildUpon();
                Backstack.Entry<?> entry = checkNotNull(builder.pop(), "Popped entry is null.");
                Backstack newBackstack = builder.build();

                notifyScreenPopped(entry.screen);
                notifyBackward(newBackstack, entry.animator);
            }
        }

        @Override
        public Transition copy() {
            return new GoBackTransition();
        }
    }

    private class ReplaceWithTransition extends Transition {

        @NonNull
        private final Screen<?> screen;

        @Nullable
        private final TransitionAnimator animator;

        private ReplaceWithTransition(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
            this.screen = screen;
            this.animator = animator;
        }

        @Override
        public void execute() {
            Backstack.Builder builder = backstack.buildUpon();
            Backstack.Entry<?> entry = checkNotNull(builder.pop(), "Popped entry is null");
            builder.push(screen, animator);
            Backstack newBackstack = builder.build();

            notifyScreenPopped(entry.screen);
            notifyScreenPushed(screen);
            notifyReplace(newBackstack);
        }

        @Override
        public Transition copy() {
            return new ReplaceWithTransition(screen, animator);
        }
    }

    private class ForwardTransition extends Transition {

        @NonNull
        private final Backstack newBackstack;

        private ForwardTransition(@NonNull final Backstack newBackstack) {
            this.newBackstack = newBackstack;
        }

        @Override
        public void execute() {
            for (Screen<?> screen : backstack) {
                notifyScreenPopped(screen);
            }
            for (Iterator<Screen<?>> it = newBackstack.reverseIterator(); it.hasNext(); ) {
                notifyScreenPushed(it.next());
            }

            notifyForward(newBackstack);
        }

        @Override
        public Transition copy() {
            return new ForwardTransition(newBackstack);
        }
    }

    private class PopToTransition extends Transition {

        @NonNull
        private final Screen<?> screen;

        @Nullable
        private final TransitionAnimator animator;

        private PopToTransition(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
            this.screen = screen;
            this.animator = animator;
        }

        @Override
        public void execute() {
            if (backstack.current().screen.equals(screen)) {
                onComplete();
                return;
            }

            Backstack.Builder builder = backstack.buildUpon();
            Backstack.Builder poppedScreens = Backstack.emptyBuilder();
            int count = 0;
            // Take care to leave the original screen instance on the stack, if we find it.  This enables
            // some arguably bad behavior on the part of clients, but it's still probably the right thing
            // to do.
            for (Iterator<Backstack.Entry<?>> it = backstack.reverseEntryIterator(); it.hasNext(); ) {
                Screen<?> screen = it.next().screen;

                if (screen.equals(this.screen)) {
                    // Clear up to the target screen.
                    for (int i = 0; i < backstack.size() - count; i++) {
                        Backstack.Entry<?> entry = builder.pop();
                        if (entry != null) {
                            poppedScreens.push(entry);
                        }
                    }
                    break;
                } else {
                    count++;
                }
            }

            Backstack newBackstack;
            Backstack poppedBackstack = poppedScreens.build();
            if (poppedBackstack.size() != 0) {
                for (Iterator<Backstack.Entry<?>> it = poppedBackstack.reverseEntryIterator(); it.hasNext(); ) {
                    Backstack.Entry<?> entry = it.next();
                    notifyScreenPopped(entry.screen);
                }

                builder.push(poppedBackstack.current());
                newBackstack = builder.build();
                notifyBackward(newBackstack, animator);
            } else {
                notifyScreenPushed(screen);

                builder.push(screen, animator);
                newBackstack = builder.build();
                notifyForward(newBackstack);
            }
        }

        @Override
        public Transition copy() {
            return new PopToTransition(screen, animator);
        }
    }

    private class ShowTransition extends Transition {

        @Override
        public void execute() {
            notifyForward(backstack);
        }

        @Override
        public Transition copy() {
            return new ShowTransition();
        }
    }

    private class StartWithTransition extends Transition {

        @NonNull
        private final Screen<?> screen;

        @Nullable
        private final TransitionAnimator animator;

        private StartWithTransition(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
            this.screen = screen;
            this.animator = animator;
        }

        @Override
        public void execute() {
            Backstack newBackstack = Backstack.single(screen, animator);
            notifyScreenPushed(screen);
            notifyForward(newBackstack);
        }

        @Override
        public Transition copy() {
            return new StartWithTransition(screen, animator);
        }
    }

    private class StartWithBackstackTransition extends Transition {

        @NonNull
        private final Backstack backstack;

        private StartWithBackstackTransition(@NonNull final Backstack backstack) {
            this.backstack = backstack;
        }

        @Override
        public void execute() {
            for (Iterator<Screen<?>> iterator = backstack.reverseIterator(); iterator.hasNext(); ) {
                notifyScreenPushed(iterator.next());
            }
            notifyForward(backstack);
        }

        @Override
        public Transition copy() {
            return new StartWithBackstackTransition(backstack);
        }
    }

    private class GoToTransition extends Transition {

        @NonNull
        private final Screen<?> screen;

        @Nullable
        private final TransitionAnimator animator;

        private GoToTransition(@NonNull final Screen<?> screen, @Nullable final TransitionAnimator animator) {
            this.screen = screen;
            this.animator = animator;
        }

        @Override
        public void execute() {
            Backstack newBackstack = backstack.buildUpon().push(screen, animator).build();
            notifyScreenPushed(screen);
            notifyForward(newBackstack);
        }

        @Override
        public Transition copy() {
            return new GoToTransition(screen, animator);
        }
    }

    private class BackwardTransition extends Transition {

        @NonNull
        private final Backstack newBackstack;

        private BackwardTransition(@NonNull final Backstack newBackstack) {
            this.newBackstack = newBackstack;
        }

        @Override
        public void execute() {
            for (Screen<?> screen : backstack) {
                notifyScreenPopped(screen);
            }
            for (Iterator<Screen<?>> it = newBackstack.reverseIterator(); it.hasNext(); ) {
                notifyScreenPushed(it.next());
            }

            notifyBackward(newBackstack, null);
        }

        @Override
        public Transition copy() {
            return new BackwardTransition(newBackstack);
        }
    }

    private class ReplaceTransition extends Transition {

        @NonNull
        private final Backstack newBackstack;

        private ReplaceTransition(@NonNull final Backstack newBackstack) {
            this.newBackstack = newBackstack;
        }

        @Override
        public void execute() {
            for (Screen<?> screen : backstack) {
                notifyScreenPopped(screen);
            }
            for (Iterator<Screen<?>> it = newBackstack.reverseIterator(); it.hasNext(); ) {
                notifyScreenPushed(it.next());
            }

            notifyReplace(newBackstack);
        }

        @Override
        public Transition copy() {
            return new ReplaceTransition(newBackstack);
        }
    }
}
