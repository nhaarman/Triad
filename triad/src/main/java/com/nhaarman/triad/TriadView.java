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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.RelativeLayout;

@SuppressWarnings("AnonymousClassVariableHidesContainingMethodVariable")
public class TriadView extends RelativeLayout {

    private final long mTransitionAnimationDurationMs;

    public TriadView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriadView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        mTransitionAnimationDurationMs = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    void forward(@Nullable final ViewGroup currentView,
                 @NonNull final ViewGroup newView,
                 @Nullable final TransitionAnimator animator,
                 @NonNull final Triad.Callback callback) {
        addView(newView);
        newView.getViewTreeObserver().addOnPreDrawListener(new ForwardTransitionPreDrawListener(currentView, newView, animator, callback));
    }

    void backward(@Nullable final ViewGroup currentView,
                  @NonNull final ViewGroup newView,
                  @Nullable final TransitionAnimator animator,
                  @NonNull final Triad.Callback callback) {
        addView(newView);
        newView.getViewTreeObserver().addOnPreDrawListener(new BackwardTransitionPreDrawListener(currentView, newView, animator, callback));
    }

    private class ForwardTransitionPreDrawListener extends TransitionPreDrawListener {

        /**
         * @param currentView The {@link View} to execute an exit animation for.
         * @param newView     The {@link View} to execute an entering animation for.
         */
        ForwardTransitionPreDrawListener(@Nullable final View currentView, @NonNull final View newView, @Nullable final TransitionAnimator animator,
                                         @NonNull final Triad.Callback callback) {
            super(currentView, newView, animator, callback);
        }

        @Override
        protected boolean handleAnimation(@Nullable final View currentView, @NonNull final View newView, @Nullable final TransitionAnimator animator,
                                          @NonNull final Triad.Callback callback) {
            return animator != null && animator.forward(currentView, newView, callback);
        }
    }

    private class BackwardTransitionPreDrawListener extends TransitionPreDrawListener {

        /**
         * @param currentView The {@link View} to execute an exit animation for.
         * @param newView     The {@link View} to execute an entering animation for.
         */
        BackwardTransitionPreDrawListener(@Nullable final View currentView, @NonNull final View newView, @Nullable final TransitionAnimator animator,
                                          @NonNull final Triad.Callback callback) {
            super(currentView, newView, animator, callback);
        }

        @Override
        protected boolean handleAnimation(@Nullable final View currentView, @NonNull final View newView, @Nullable final TransitionAnimator animator,
                                          @NonNull final Triad.Callback callback) {
            return animator != null && animator.backward(currentView, newView, callback);
        }
    }

    /**
     * Observes when the new {@link View} is about to be drawn, so a replacement animation can be executed.
     */
    private abstract class TransitionPreDrawListener implements OnPreDrawListener {

        @Nullable
        private final View mCurrentView;

        @NonNull
        private final View mNewView;

        @Nullable
        private final TransitionAnimator mAnimator;

        @NonNull
        private final Triad.Callback mCallback;

        /**
         * @param currentView The {@link View} to execute an exit animation for.
         * @param newView     The {@link View} to execute an entering animation for.
         */
        TransitionPreDrawListener(@Nullable final View currentView,
                                  @NonNull final View newView,
                                  @Nullable final TransitionAnimator animator,
                                  @NonNull final Triad.Callback callback) {
            mCurrentView = currentView;
            mNewView = newView;
            mAnimator = animator;
            mCallback = callback;
        }

        @Override
        public boolean onPreDraw() {
            mNewView.getViewTreeObserver().removeOnPreDrawListener(this);

            boolean handled = handleAnimation(mCurrentView, mNewView, mAnimator, mCallback);
            if (!handled) {
                animateViewReplacing();
            }

            return true;
        }

        protected abstract boolean handleAnimation(@Nullable final View currentView, @NonNull final View newView, @Nullable final TransitionAnimator animator,
                                                   @NonNull final Triad.Callback callback);

        /**
         * Executes an exit animation for the old {@link View}, and an entering animation for the new {@link View}.
         * The old {@link View} will be removed from its parent after the animation.
         */
        private void animateViewReplacing() {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
            anim.setDuration(mTransitionAnimationDurationMs);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation) {
                    animateAlphas((float) animation.getAnimatedValue());
                }
            });
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animation) {
                    finishAnimation();
                }
            });
            anim.start();
        }

        private void animateAlphas(final float value) {
            if (mCurrentView != null) {
                mCurrentView.setAlpha(1 - value);
            }
            mNewView.setAlpha(value);
        }

        private void finishAnimation() {
            if (mCurrentView != null) {
                ((ViewManager) mCurrentView.getParent()).removeView(mCurrentView);
            }

            mCallback.onComplete();
        }
    }
}
