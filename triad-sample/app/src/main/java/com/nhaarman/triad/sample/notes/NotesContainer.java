package com.nhaarman.triad.sample.notes;

import com.nhaarman.triad.ScreenContainer;
import com.nhaarman.triad.sample.notes.noteslist.NotesListContainer;
import org.jetbrains.annotations.NotNull;

interface NotesContainer extends ScreenContainer<NotesPresenter, NotesContainer> {

  @NotNull
  NotesListContainer getNotesListContainer();
}
