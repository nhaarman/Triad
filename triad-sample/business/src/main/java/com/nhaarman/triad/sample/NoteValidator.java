package com.nhaarman.triad.sample;

import org.jetbrains.annotations.Nullable;

public class NoteValidator {

  public boolean validateTitle(@Nullable final String title) {
    return title != null && !title.trim().isEmpty();
  }

  public boolean validateContents(final String contents) {
    return contents != null && !contents.trim().isEmpty();
  }
}
