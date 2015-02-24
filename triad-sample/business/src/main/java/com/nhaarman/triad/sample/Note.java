package com.nhaarman.triad.sample;

public class Note implements Comparable<Note> {

  private String mTitle;
  private String mContents;
  private long mCreated;

  public String getTitle() {
    return mTitle;
  }

  public void setTitle(final String title) {
    mTitle = title;
  }

  public String getContents() {
    return mContents;
  }

  public void setContents(final String contents) {
    mContents = contents;
  }

  public long getCreated() {
    return mCreated;
  }

  public void setCreated(final long created) {
    mCreated = created;
  }

  @Override
  public int compareTo(final Note another) {
    return Long.valueOf(mCreated).compareTo(another.getCreated());
  }
}
