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

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * An [AppCompatActivity] which is the root of an application that uses Triad.
 *
 * @param ApplicationComponent The `ApplicationComponent` to use for `Presenter` creation.
 */
abstract class TriadAppCompatActivity<ApplicationComponent : Any> : AppCompatActivity() {

    private val delegate: TriadDelegate<ApplicationComponent> by lazy {
        TriadDelegate.createFor<ApplicationComponent>(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate(intent)
    }

    override fun onResume() {
        super.onResume()
        delegate.onResume()
    }

    override fun onBackPressed() {
        if (!delegate.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        delegate.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()
        delegate.onPause()
    }

    override fun onDestroy() {
        delegate.onDestroy()
        super.onDestroy()
    }

    /**
     * Returns the [Triad] instance to be used to navigate between [Screen]s.
     */
    protected val triad: Triad by lazy { delegate.triad }

    protected fun setOnScreenChangedListener(onScreenChangedListener: OnScreenChangedListener<ApplicationComponent>?) {
        delegate.setOnScreenChangedListener(onScreenChangedListener)
    }
}
