/*
 * Copyright 2016 Niek Haarman
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

package com.nhaarman.triad

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup

/**
 * This class represents a delegate which can be used to use Triad in any
 * [Activity].
 *
 *
 * When using the `TriadDelegate`, you must proxy the following Activity
 * lifecycle methods to it:
 *
 *  - [.onCreate]
 *  - [.onResume]
 *  - [.onPause]
 *  - [.onDestroy]
 *  - [.onBackPressed]
 *  - [.onActivityResult]
 *

 * @param ApplicationComponent The `ApplicationComponent` to use for `Presenter` creation.
 */
class TriadDelegate<ApplicationComponent : Any> internal constructor(
      private val activity: Activity,
      private val defaultTransitionAnimator: TransitionAnimator
) {

    private var resumed = false

    @Suppress("UNCHECKED_CAST")
    private val applicationComponent: ApplicationComponent by lazy {
        if (activity.application !is ApplicationComponentProvider<*>) {
            throw IllegalStateException("Make sure your Application class implements ApplicationComponentProvider.")
        }

        (activity.application as ApplicationComponentProvider<ApplicationComponent>).applicationComponent
    }

    private val rootView: ViewGroup by lazy {
        activity.findViewById(android.R.id.content) as ViewGroup
    }

    val triad: Triad by lazy {
        if (activity.application !is TriadProvider) {
            throw IllegalStateException("Make sure your Application class implements TriadProvider.")
        }

        (activity.application as TriadProvider).triad
    }

    val currentScreen: Screen<ApplicationComponent>?
        get() = triad.backstack.current<ApplicationComponent>()?.screen

    /**
     * An optional [OnScreenChangedListener] that is notified of screen changes.
     */
    var onScreenChangedListener: OnScreenChangedListener<ApplicationComponent>? = null

    fun onCreate() {
        triad.setActivity(activity)
        triad.setListener(MyTriadListener())

        if (triad.backstack.size() > 0) {
            triad.showCurrent()
        }
    }

    fun onResume() {
        currentScreen?.onAttach(activity)
        resumed = true
    }

    fun onBackPressed(): Boolean {
        return currentScreen != null && currentScreen!!.onBackPressed() || triad.goBack()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        triad.onActivityResult(requestCode, resultCode, data)
    }

    fun onPause() {
        resumed = false
        currentScreen?.onDetach(activity)
    }

    fun onDestroy() {
        if (!activity.isFinishing) return

        val iterator = triad.backstack.reverseIterator()
        while (iterator.hasNext()) {
            val screen = iterator.next()
            screen.onDestroy()
        }
    }

    private inner class MyTriadListener : Triad.Listener<ApplicationComponent> {

        override fun screenPushed(pushedScreen: Screen<ApplicationComponent>) {
            pushedScreen.setApplicationComponent(applicationComponent)
            pushedScreen.onCreate()
            if (resumed) pushedScreen.onAttach(activity)
        }

        override fun screenPopped(poppedScreen: Screen<ApplicationComponent>) {
            if (resumed) poppedScreen.onDetach(activity)
            poppedScreen.onDestroy()
        }

        override fun forward(newScreen: Screen<ApplicationComponent>, animator: TransitionAnimator?, onComplete: () -> Unit) {
            rootView.getChildAt(0)?.let { view ->
                currentScreen?.saveState(view)
            }

            val oldView = rootView.getChildAt(0)
            val newView = newScreen.createView(rootView)

            val handled = animator?.forward(oldView, newView, rootView, {
                if (oldView?.parent != null) (oldView.parent as ViewGroup).removeView(oldView)
                onComplete()
            })

            if (handled != true) {
                defaultTransitionAnimator.forward(oldView, newView, rootView, onComplete)
            }

            onScreenChangedListener?.onScreenChanged(newScreen)
        }

        override fun backward(newScreen: Screen<ApplicationComponent>, animator: TransitionAnimator?, onComplete: () -> Unit) {
            val oldView = rootView.getChildAt(0)
            val newView = newScreen.createView(rootView).apply {
                newScreen.restoreState(this)
            }

            val handled = animator?.backward(oldView, newView, rootView, {
                if (oldView?.parent != null) (oldView.parent as ViewGroup).removeView(oldView)
                onComplete()
            })

            if (handled != true) {
                defaultTransitionAnimator.backward(oldView, newView, rootView, onComplete)
            }

            onScreenChangedListener?.onScreenChanged(newScreen)
        }

        override fun replace(newScreen: Screen<ApplicationComponent>, animator: TransitionAnimator?, onComplete: () -> Unit) {
            val oldView = rootView.getChildAt(0)
            val newView = newScreen.createView(rootView)

            val handled = animator?.forward(oldView, newView, rootView, {
                if (oldView?.parent != null) (oldView.parent as ViewGroup).removeView(oldView)
                onComplete()
            })

            if (handled != true) {
                defaultTransitionAnimator.forward(oldView, newView, rootView, onComplete)
            }

            onScreenChangedListener?.onScreenChanged(newScreen)
        }
    }

    companion object {

        @JvmStatic
        fun <T : Any> createFor(activity: Activity): TriadDelegate<T> {
            return TriadDelegate(activity, DefaultTransitionAnimator)
        }
    }
}
