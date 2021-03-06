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

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * An abstract RelativeLayout [Container] instance that handles [Presenter] management
 * for use in an adapter View.
 */
abstract class AdapterRelativeLayoutContainer @JvmOverloads constructor(
      context: Context,
      attrs: AttributeSet?,
      defStyle: Int = 0
) : RelativeLayout(context, attrs, defStyle),
      AdapterContainer {

    private var attachedToWindow: Boolean = false

    private var _presenter: Presenter<AdapterContainer>? = null
        set(value) {
            field?.release()
            field = value
            value?.let {
                if (attachedToWindow) {
                    it.acquire(this)
                }
            }
        }

    @Suppress("UNCHECKED_CAST")
    override fun setPresenter(presenter: Presenter<*>?) {
        _presenter = presenter as Presenter<AdapterContainer>
    }

    @Suppress("UNCHECKED_CAST")
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        _presenter?.acquire(this)

        attachedToWindow = true
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        _presenter?.release()

        attachedToWindow = false
    }
}
