package com.nhaarman.triad.sample.notes;

import com.nhaarman.triad.presenter.ScreenPresenter;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.editnote.EditNoteScreen;
import com.nhaarman.triad.sample.notes.noteslist.NotesListPresenter;
import org.jetbrains.annotations.NotNull;

class NotesPresenter extends ScreenPresenter<NotesPresenter, NotesContainer> implements NotesListPresenter.OnNoteClickedListener {

  @NotNull
  private final NotesListPresenter mNotesListPresenter;

  NotesPresenter(@NotNull final NotesListPresenter notesListPresenter) {
    mNotesListPresenter = notesListPresenter;
    mNotesListPresenter.setNoteClickedListener(this);
  }

  @Override
  protected void onControlGained(@NotNull final NotesContainer container) {
    container.getNotesListContainer().setPresenter(mNotesListPresenter);
  }

  public void onCreateNoteClicked() {
    getFlow().goTo(new EditNoteScreen());
  }

  @Override
  public void onNoteClicked(@NotNull final Note note) {
    getFlow().goTo(new EditNoteScreen(note));
  }
}
