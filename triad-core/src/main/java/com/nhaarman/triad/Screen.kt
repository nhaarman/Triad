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

package com.nhaarman.triad

import android.os.Parcelable
import android.support.annotation.LayoutRes
import android.util.SparseArray
import android.view.*

abstract class Screen<ApplicationComponent : Any> {

    private val state = SparseArray<Parcelable>()

    lateinit var applicationComponent: ApplicationComponent

    @get:LayoutRes
    protected abstract val layoutResId: Int

    open fun createView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
    }

    abstract val presenter: Presenter<*>

    open fun saveState(view: View) {
        view.saveHierarchyState(state)
    }

    open fun restoreState(view: View) {
        view.restoreHierarchyState(state)
    }

    var rootView: ViewGroup? = null
        set(value) {
            if (field == value) return

            if (field != null) {
                detach()
            }

            if (value != null) {
                attach(value)
            }
        }

    open fun onCreate() {}

    abstract fun attach(root: ViewGroup)

    open fun onBackPressed(): Boolean {
        return false
    }

    abstract fun detach()

    open fun onDestroy() {}
}
