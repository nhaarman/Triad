/*
 * Copyright 2013 Square Inc.
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
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;

/**
 * Describes the history of a {@link Triad} at a specific point in time.
 */
public class Backstack implements Iterable<Screen<?>> {

  @NonNull
  private final Deque<Screen<?>> mBackstack;

  private Backstack(@NonNull final Deque<Screen<?>> backstack) {
    mBackstack = backstack;
  }

  @Override
  public Iterator<Screen<?>> iterator() {
    return new ReadIterator<>(mBackstack.iterator());
  }

  public Iterator<Screen<?>> reverseIterator() {
    return new ReadIterator<>(mBackstack.descendingIterator());
  }

  public int size() {
    return mBackstack.size();
  }

  @NonNull
  public <T> Screen<T> current() {
    return (Screen<T>) mBackstack.peek();
  }

  /**
   * Get a builder to modify a copy of this backstack.
   */
  public Builder buildUpon() {
    return new Builder(mBackstack);
  }

  @Override
  public String toString() {
    return mBackstack.toString();
  }

  public static Builder emptyBuilder() {
    return new Builder(Collections.<Screen<?>>emptyList());
  }

  /**
   * Create a backstack that contains a single screen.
   */
  public static Backstack single(@NonNull final Screen<?> screen) {
    return emptyBuilder().push(screen).build();
  }

  /**
   * Creates a backstack that contains given screens.
   */
  public static Backstack of(@NonNull final Screen<?>... screens) {
    Builder builder = emptyBuilder();

    for (Screen<?> screen : screens) {
      builder.push(screen);
    }

    return builder.build();
  }

  public static final class Builder {

    @NonNull
    private final Deque<Screen<?>> mBackstack;

    Builder(@NonNull final Collection<Screen<?>> backstack) {
      mBackstack = new ArrayDeque<>(backstack);
    }

    @NonNull
    public Builder push(@NonNull final Screen<?> screen) {
      mBackstack.push(screen);

      return this;
    }

    @NonNull
    public Builder addAll(@NonNull final Collection<Screen<?>> screens) {
      for (Screen<?> screen : screens) {
        mBackstack.push(screen);
      }

      return this;
    }

    @Nullable
    public Screen<?> peek() {
      return mBackstack.peek();
    }

    @Nullable
    public Screen<?> pop() {
      return mBackstack.pop();
    }

    @NonNull
    public Builder clear() {
      mBackstack.clear();

      return this;
    }

    @NonNull
    public Backstack build() {
      return new Backstack(mBackstack);
    }
  }

  private static class ReadIterator<T> implements Iterator<T> {

    private final Iterator<T> mIterator;

    ReadIterator(final Iterator<T> iterator) {
      mIterator = iterator;
    }

    @Override
    public boolean hasNext() {
      return mIterator.hasNext();
    }

    @Override
    public T next() {
      return mIterator.next();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
