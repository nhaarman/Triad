/*
 * Copyright 2015 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.triad;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * The Container interface.
 *
 * @param <P> The specialized type of the {@link Presenter} for this {@link Container}.
 * @param <C> The specialized type of the {@link Container}.
 */
public interface Container<P extends Presenter<P, C>, C extends Container<P, C>> {

  /**
   * Sets the {@link Presenter} that should control this {@code Container}.
   * Implementers must ensure that {@link Presenter#acquire(Container)}  and {@link Presenter#releaseContainer()}
   * are called at proper times.
   *
   * @param presenter The {@link Presenter} instance.
   */
  void setPresenter(@NonNull P presenter);

  /**
   * Returns the context the container is running in, through which it can
   * access the current theme, resources, etc.
   *
   * @return The container's Context.
   */
  @NonNull
  Context getContext();
}
