package com.nhaarman.triad;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import com.nhaarman.triad.Triad.Callback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class DefaultTransitionAnimator implements TransitionAnimator {

    public static final DefaultTransitionAnimator INSTANCE = new DefaultTransitionAnimator();

    private DefaultTransitionAnimator() {
    }

    @Override
    public boolean forward(@Nullable final View oldView, @NotNull final View newView, @NotNull final ViewGroup parent, @NotNull final Callback callback) {
        executeAlphaTransition(oldView, newView, parent, callback);
        return true;
    }

    @Override
    public boolean backward(@Nullable final View oldView, @NotNull final View newView, @NotNull final ViewGroup parent, @NotNull final Callback callback) {
        executeAlphaTransition(oldView, newView, parent, callback);
        return true;
    }

    private void executeAlphaTransition(
          @Nullable final View oldView,
          @NonNull final View newView,
          @NonNull final ViewGroup parent,
          @NonNull final Callback callback
    ) {
        int durationMs = parent.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);

        parent.addView(newView);

        if (oldView != null) {
            oldView.animate()
                  .alpha(0f)
                  .setDuration(durationMs);
        }

        newView.setAlpha(0f);
        newView.animate()
              .alpha(1f)
              .setDuration(durationMs)
              .withEndAction(new Runnable() {
                  @Override
                  public void run() {
                      if (oldView != null) {
                          parent.removeView(oldView);
                      }

                      callback.onComplete();
                  }
              });
    }
}
