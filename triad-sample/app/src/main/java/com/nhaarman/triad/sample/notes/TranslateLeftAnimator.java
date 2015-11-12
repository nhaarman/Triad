package com.nhaarman.triad.sample.notes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.nhaarman.triad.TransitionAnimator;
import com.nhaarman.triad.Triad;

class TranslateLeftAnimator implements TransitionAnimator {

  @Override
  public boolean forward(@Nullable final View oldView, @NonNull final View newView, @NonNull final Triad.Callback callback) {
    if (oldView == null) {
      return false;
    }

    oldView.animate().x(-oldView.getWidth()).setInterpolator(new AccelerateInterpolator());
    newView.setX(((View) newView.getParent()).getWidth());
    newView.animate().x(0).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(final Animator animation) {
        newView.animate().setListener(null);
        ((ViewManager) oldView.getParent()).removeView(oldView);

        callback.onComplete();
      }
    });

    return true;
  }

  @Override
  public boolean backward(@Nullable final View oldView, @NonNull final View newView, @NonNull final Triad.Callback callback) {
    if (oldView == null) {
      return false;
    }

    newView.setX(-((View) newView.getParent()).getWidth());
    newView.animate().x(0).setInterpolator(new AccelerateInterpolator());
    oldView.animate().x(((View) oldView.getParent()).getWidth()).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(final Animator animation) {
        oldView.animate().setListener(null);
        ((ViewManager) oldView.getParent()).removeView(oldView);

        callback.onComplete();
      }
    });

    return true;
  }
}