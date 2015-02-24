package com.nhaarman.triad.sample.notes.noteslist;

import com.nhaarman.triad.presenter.Presenter;
import com.nhaarman.triad.sample.Note;
import org.jetbrains.annotations.NotNull;

public class NotePresenter extends Presenter<NotePresenter, NoteContainer> {

  public void setNote(@NotNull final Note note) {
    if(getContainer() == null){
      return;
    }

    getContainer().setTitle(note.getTitle());
    getContainer().setContents(note.getContents());
  }
}
