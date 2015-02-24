package com.nhaarman.triad.sample.notes.noteslist;

import com.nhaarman.triad.container.Container;
import org.jetbrains.annotations.NotNull;

public interface NoteContainer extends Container<NotePresenter, NoteContainer> {

  void setTitle(@NotNull String title);

  void setContents(@NotNull String contents);

  @NotNull
  NotePresenter getPresenter();
}
