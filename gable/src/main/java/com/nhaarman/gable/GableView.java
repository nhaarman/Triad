package com.nhaarman.gable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import com.nhaarman.gable.container.RelativeLayoutContainer;
import com.nhaarman.gable.screen.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link View}, {@link GableContainer}, which hosts all {@link View}s belonging to {@link Screen}s in the application.
 *
 * @param <M> The main module in the application. See {@link GablePresenter}.
 */
class GableView<M> extends RelativeLayoutContainer<GablePresenter<M>, GableContainer<M>> implements GableContainer<M> {

  GableView(final Context context) {
    super(context);
  }

  @Override
  public void transition(@Nullable final View oldView, @Nullable final View newView) {
    if (newView != null) {
      addView(newView);
      newView.getViewTreeObserver().addOnPreDrawListener(new TransitionPreDrawListener(oldView, newView));
    } else if (oldView != null) {
      animateViewExit(oldView);
    }
  }

  /**
   * Executes an exit animation for given {@link View},
   * after which the {@link View} will be removed from its parent.
   *
   * @param view The {@link View} to animate and remove.
   */
  private void animateViewExit(@NotNull final View view) {
    ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 0);
    alphaAnimator.addListener(new TransitionAnimatorListener(view));
    alphaAnimator.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    alphaAnimator.start();
  }

  /**
   * Observes when the new {@link View} is about to be drawn, so a replacement animation can be executed.
   */
  private class TransitionPreDrawListener implements ViewTreeObserver.OnPreDrawListener {

    @Nullable
    private final View mOldView;

    @NotNull
    private final View mNewView;

    /**
     * @param oldView The {@link View} to execute an exit animation for.
     * @param newView The {@link View} to execute an entering animation for.
     */
    TransitionPreDrawListener(@Nullable final View oldView, @NotNull final View newView) {
      mOldView = oldView;
      mNewView = newView;
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
        mOldView.animate().alpha(0).setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime)).start();
      }

      mNewView.setAlpha(0);
      ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mNewView, View.ALPHA, 1);
      alphaAnimator.addListener(new TransitionAnimatorListener(mOldView));
      alphaAnimator.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
      alphaAnimator.start();
    }
  }

  /**
   * An {@link Animator.AnimatorListener} which removes the old {@link View} from its parent when finished.
   */
  private class TransitionAnimatorListener extends AnimatorListenerAdapter {

    @Nullable
    private final View mOldView;

    private TransitionAnimatorListener(@Nullable final View oldView) {
      mOldView = oldView;
    }

    @Override
    public void onAnimationEnd(final Animator animation) {
      if (mOldView != null) {
        removeView(mOldView);
      }
    }
  }
}
