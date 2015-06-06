package com.nhaarman.triad;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewTreeObserver.OnPreDrawListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link View}, {@link TriadContainer}, which hosts all {@link View}s belonging to {@link Screen}s in the application.
 *
 * @param <M> The main module in the application. See {@link TriadPresenter}.
 */
public class TriadView<M> extends RelativeLayoutContainer<TriadPresenter<M>, TriadContainer<M>> implements TriadContainer<M> {

  private final long mTransitionAnimationDurationMs;

  @Nullable
  private ViewGroup mScreenHolder;

  public TriadView(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TriadView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);

    mTransitionAnimationDurationMs = retrieveTransitionAnimationDurationMs();
  }

  public TriadView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    mTransitionAnimationDurationMs = retrieveTransitionAnimationDurationMs();
  }

  private long retrieveTransitionAnimationDurationMs() {
    return getResources().getInteger(android.R.integer.config_shortAnimTime);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    mScreenHolder = (ViewGroup) findViewById(R.id.view_triad_screenholder);
  }

  @Override
  public void transition(@Nullable final View oldView, @Nullable final View newView) {
    if (mScreenHolder == null) {
      throw new NullPointerException("Calling transition(View, View) before onFinishInflate() was called.");
    }

    if (newView != null) {
      mScreenHolder.addView(newView);
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
        }
      });
      animator.start();
    }
  }
}
