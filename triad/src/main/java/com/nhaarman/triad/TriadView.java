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
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewManager;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.RelativeLayout;

import static com.nhaarman.triad.Preconditions.checkArgument;

public class TriadView extends RelativeLayout {

  private final long mTransitionAnimationDurationMs;

  public TriadView(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TriadView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);

    mTransitionAnimationDurationMs = retrieveTransitionAnimationDurationMs();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public TriadView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    mTransitionAnimationDurationMs = retrieveTransitionAnimationDurationMs();
  }

  private long retrieveTransitionAnimationDurationMs() {
    return getResources().getInteger(android.R.integer.config_shortAnimTime);
  }

  public void transition(@Nullable final View oldView, @Nullable final View newView, @NonNull final Triad.Callback callback) {
    checkArgument(oldView != null || newView != null, "Both oldView and newView are null.");

    if (newView != null) {
      addView(newView);
      newView.getViewTreeObserver().addOnPreDrawListener(new TransitionPreDrawListener(oldView, newView, callback));
    } else {
      animateViewExit(oldView);
    }
  }

  /**
   * Executes an exit animation for given {@link View},
   * after which the {@link View} will be removed from its parent.
   *
   * @param view The {@link View} to animate and remove.
   */
  private void animateViewExit(@NonNull final View view) {
    ObjectAnimator animator = ObjectAnimator.ofFloat(view, ALPHA, 0f);
    animator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(final Animator animation) {
        ((ViewManager) view.getParent()).removeView(view);
      }
    });
    animator.start();
  }

  /**
   * Observes when the new {@link View} is about to be drawn, so a replacement animation can be executed.
   */
  private class TransitionPreDrawListener implements OnPreDrawListener {

    @Nullable
    private final View mOldView;

    @NonNull
    private final View mNewView;

    @NonNull
    private final Triad.Callback mCallback;

    /**
     * @param oldView The {@link View} to execute an exit animation for.
     * @param newView The {@link View} to execute an entering animation for.
     */
    TransitionPreDrawListener(@Nullable final View oldView,
                              @NonNull final View newView,
                              @NonNull final Triad.Callback callback) {
      mOldView = oldView;
      mNewView = newView;
      mCallback = callback;
    }

    @Override
    public boolean onPreDraw() {
      mNewView.getViewTreeObserver().removeOnPreDrawListener(this);
      animateViewReplacing();
      return true;
    }

    /**
     * Executes an exit animation for the old {@link View}, and an entering animation for the new {@link View}.
     * The old {@link View} will be removed from its parent after the animation.
     */
    private void animateViewReplacing() {
      if (mOldView != null) {
        mOldView.animate()
            .alpha(0)
            .setDuration(mTransitionAnimationDurationMs);
      }

      mNewView.setAlpha(0);

      ObjectAnimator animator = ObjectAnimator.ofFloat(mNewView, ALPHA, 1f);
      animator.setDuration(mTransitionAnimationDurationMs);
      animator.addListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(final Animator animation) {
          if (mOldView != null) {
            ((ViewManager) mOldView.getParent()).removeView(mOldView);
          }

          mCallback.onComplete();
        }
      });
      animator.start();
    }
  }
}
