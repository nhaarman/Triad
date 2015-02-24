package com.nhaarman.triad.sample.notes.noteslist;

import com.nhaarman.triad.container.Container;
import com.nhaarman.triad.sample.Note;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface NotesListContainer extends Container<NotesListPresenter, NotesListContainer> {

  void setNotes(@NotNull List<Note> notes);
}
