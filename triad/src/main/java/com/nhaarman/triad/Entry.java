package com.nhaarman.triad;

import org.jetbrains.annotations.NotNull;

public final class Entry {

  private final long mId;

  @NotNull
  private final Screen<?, ?, ?> mScreen;

  Entry(final long id, @NotNull final Screen<?, ?, ?> screen) {
    mId = id;
    mScreen = screen;
  }

  public long getId() {
    return mId;
  }

  @NotNull
  public Screen<?, ?, ?> getScreen() {
    return mScreen;
  }

  @Override
  public String toString() {
    return "{" + mId + ", " + mScreen + '}';
  }
}
