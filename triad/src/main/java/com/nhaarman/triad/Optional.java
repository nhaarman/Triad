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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.nhaarman.triad.Preconditions.checkNotNull;

public class Optional<T> {

  private static final Optional<?> EMPTY = new Optional<>();

  private final T mValue;

  private Optional() {
    mValue = null;
  }

  private Optional(@NonNull final T value) {
    checkNotNull(value, "value is null.");
    mValue = value;
  }

  public void ifPresent(@NonNull final Consumer<? super T> consumer) {
    if (mValue != null) {
      consumer.accept(mValue);
    }
  }

  public boolean isPresent() {
    return mValue != null;
  }

  public T get() {
    return checkNotNull(mValue, "value not present.");
  }

  public static <T> Optional<T> of(@Nullable final T value) {
    return value == null ? (Optional<T>) EMPTY : new Optional<>(value);
  }

  public static <T> Optional<T> empty() {
    return (Optional<T>) EMPTY;
  }

  public interface Consumer<T> {

    void accept(@NonNull T t);
  }
}
