package com.nhaarman.triad.sample.notes;

import com.nhaarman.triad.presenter.ScreenPresenter;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.editnote.EditNoteScreen;
import com.nhaarman.triad.sample.notes.noteslist.NotesListPresenter;
import flow.Flow;
import org.jetbrains.annotations.NotNull;

class NotesPresenter extends ScreenPresenter<NotesPresenter, NotesContainer> implements NotesListPresenter.OnNoteClickedListener {

  @NotNull
  private final Flow mFlow;

  @NotNull
  private final NotesListPresenter mNotesListPresenter;

  NotesPresenter(@NotNull final Flow flow,
                 @NotNull final NotesListPresenter notesListPresenter) {
    mFlow = flow;
    mNotesListPresenter = notesListPresenter;
    mNotesListPresenter.setNoteClickedListener(this);
  }

  @Override
  protected void onControlGained(@NotNull final NotesContainer container) {
    container.getNotesListContainer().setPresenter(mNotesListPresenter);
  }

  public void onCreateNoteClicked() {
    mFlow.goTo(new EditNoteScreen());
  }

  @Override
  public void onNoteClicked(@NotNull final Note note) {
    mFlow.goTo(new EditNoteScreen(note));
  }
}
