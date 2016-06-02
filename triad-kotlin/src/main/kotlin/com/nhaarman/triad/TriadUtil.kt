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
import android.content.Context
import android.content.ContextWrapper
import android.view.View


@Suppress("UNCHECKED_CAST")
fun <ActivityComponent> findActivityComponent(context: Context, view: View): ActivityComponent {
    if (view.isInEditMode) return null as ActivityComponent

    var baseContext = context
    while (baseContext !is Activity && baseContext is ContextWrapper) {
        baseContext = baseContext.baseContext
    }

    if (baseContext is ActivityComponentProvider<*>) {
        return (baseContext as ActivityComponentProvider<ActivityComponent>).activityComponent
    } else {
        throw Error("Make sure ${baseContext.javaClass.canonicalName} implements ActivityComponentProvider.")
    }
}

@Suppress("UNCHECKED_CAST", "PLATFORM_CLASS_MAPPED_TO_KOTLIN")
fun <P : Presenter<*, *>> findPresenter(context: Context, view: View): P {
    if (view.isInEditMode) return null as P

    var baseContext = context
    while (baseContext !is Activity && baseContext is ContextWrapper) {
        baseContext = baseContext.baseContext
    }

    if (baseContext is ScreenProvider<*>) {
        try {
            return baseContext.currentScreen?.getPresenter(view.id) as P
        } catch(t: Throwable) {
            val e = PresenterCreationFailedError("Could not create presenter for:\n    $view\nCaused by: $t", t.cause)
            (e as java.lang.Throwable).stackTrace = t.stackTrace
            throw e
        }
    } else {
        throw PresenterCreationFailedError("Make sure ${baseContext.javaClass.canonicalName} implements ScreenProvider.")
    }
}

private class PresenterCreationFailedError @JvmOverloads constructor(message: String, cause: Throwable? = null) :
      Error(message, cause)
