package com.nhaarman.triad.sample.notes.noteslist;

import com.nhaarman.triad.presenter.Presenter;
import com.nhaarman.triad.sample.Note;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NotePresenter extends Presenter<NotePresenter, NoteContainer> {

  @Nullable
  private Note mNote;

  @Override
  protected void onControlGained(@NotNull final NoteContainer container) {
    if (mNote != null) {
      setNote(mNote);
    }
  }

  public void setNote(@NotNull final Note note) {
    mNote = note;
    if (getContainer() == null) {
      return;
    }

    getContainer().setTitle(note.getTitle());
    getContainer().setContents(note.getContents());
  }
}
