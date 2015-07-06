/*
 * Copyright 2013 Square Inc.
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

package com.nhaarman.triad;

import com.nhaarman.triad.Backstack.Entry;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Describes the history of a {@link Triad} at a specific point in time.
 */
public final class Backstack implements Iterable<Entry> {

  private final long mHighestId;

  private final Deque<Entry> mBackstack;

  private Backstack(final long highestId, final Deque<Entry> backstack) {
    mHighestId = highestId;
    mBackstack = backstack;
  }

  public long getHighestId() {
    return mHighestId;
  }

  @Override
  public Iterator<Entry> iterator() {
    return new ReadIterator<>(mBackstack.iterator());
  }

  public Iterator<Entry> reverseIterator() {
    return new ReadIterator<>(mBackstack.descendingIterator());
  }

  public int size() {
    return mBackstack.size();
  }

  @Nullable
  public Entry current() {
    return mBackstack.peek();
  }

  /**
   * Get a builder to modify a copy of this backstack.
   */
  public Builder buildUpon() {
    return new Builder(mHighestId, mBackstack);
  }

  @Override
  public String toString() {
    return mBackstack.toString();
  }

  public static Builder emptyBuilder() {
    return new Builder(-1, Collections.<Entry>emptyList());
  }

  /**
   * Create a backstack that contains a single screen.
   */
  public static Backstack single(final Screen<?, ?, ?> screen) {
    return emptyBuilder().push(screen).build();
  }

  public static final class Builder {

    @NonNull
    private final Deque<Entry> mBackstack;

    private long mHighestId;

    Builder(final long highestId, @NonNull final Collection<Entry> backstack) {
      mHighestId = highestId;
      mBackstack = new ArrayDeque<>(backstack);
    }

    @NonNull
    public Builder push(@NonNull final Screen<?, ?, ?> screen) {
      mHighestId++;
      mBackstack.push(new Entry(mHighestId, screen));

      return this;
    }

    @NonNull
    public Builder addAll(@NonNull final Collection<Screen<?, ?, ?>> screens) {
      for (Screen<?, ?, ?> screen : screens) {
        mHighestId++;
        mBackstack.push(new Entry(mHighestId, screen));
      }

      return this;
    }

    @Nullable
    public Entry peek() {
      return mBackstack.peek();
    }

    @Nullable
    public Entry pop() {
      return mBackstack.pop();
    }

    @NonNull
    public Builder clear() {
      mBackstack.clear();

      return this;
    }

    @NonNull
    public Backstack build() {
      return new Backstack(mHighestId, mBackstack);
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

  @SuppressWarnings("PublicInnerClass")
  public static class Entry {

    private final long mId;

    @NonNull
    private final Screen<?, ?, ?> mScreen;

    private Entry(final long id, @NonNull final Screen<?, ?, ?> screen) {
      mId = id;
      mScreen = screen;
    }

    public long getId() {
      return mId;
    }

    @NonNull
    public Screen<?, ?, ?> getScreen() {
      return mScreen;
    }

    @Override
    public String toString() {
      return "{" + mId + ", " + mScreen + '}';
    }
  }
}
