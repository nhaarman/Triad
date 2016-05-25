package com.nhaarman.triad

import android.view.View
import android.view.ViewGroup

internal object DefaultTransitionAnimator : TransitionAnimator {

    override fun forward(oldView: View?, newView: View, parent: ViewGroup, onComplete: () -> Unit): Boolean {
        executeAlphaTransition(oldView, newView, parent, onComplete)
        return true
    }

    override fun backward(oldView: View?, newView: View, parent: ViewGroup, onComplete: () -> Unit): Boolean {
        executeAlphaTransition(oldView, newView, parent, onComplete)
        return true
    }

    private fun executeAlphaTransition(oldView: View?, newView: View, parent: ViewGroup, onComplete: () -> Unit) {
        val durationMs = parent.context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        parent.addView(newView)

        oldView?.animate()
              ?.alpha(0f)
              ?.setDuration(durationMs)

        newView.alpha = 0f
        newView.animate()
              .alpha(1f)
              .setDuration(durationMs)
              .withEndAction {
                  if (oldView != null) {
                      parent.removeView(oldView)
                  }

                  onComplete()
              }
    }
}
