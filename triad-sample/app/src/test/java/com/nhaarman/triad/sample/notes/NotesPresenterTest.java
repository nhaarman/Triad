package com.nhaarman.triad.sample.notes;

import com.nhaarman.triad.Triad;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.editnote.EditNoteScreen;
import com.nhaarman.triad.sample.notes.noteslist.NotesListPresenter;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NotesPresenterTest {

  private NotesPresenter mNotesPresenter;

  private NotesListPresenter mNotesListPresenterMock;

  private Triad mTriad;

  @Before
  public void setUp() {
    mTriad = mock(Triad.class);

    mNotesListPresenterMock = mock(NotesListPresenter.class);
    mNotesPresenter = new NotesPresenter(mNotesListPresenterMock);
    mNotesPresenter.setTriad(mTriad);
  }

  @Test
  public void onInitialization_aNoteClickedListenerIsRegistered() {
    verify(mNotesListPresenterMock).setNoteClickedListener(any(NotesListPresenter.OnNoteClickedListener.class));
  }

  @Test
  public void onCreateNoteClicked_goesToEditNoteScreen() {
    /* When */
    mNotesPresenter.onCreateNoteClicked();

    /* Then */
    verify(mTriad).goTo(any(EditNoteScreen.class));
  }

  @Test
  public void onNoteClicked_goesToEditNoteScreen() {
    /* Given */
    Note note = new Note();

    /* When */
    mNotesPresenter.onNoteClicked(note);

    /* Then */
    verify(mTriad).goTo(any(EditNoteScreen.class));
  }
}
