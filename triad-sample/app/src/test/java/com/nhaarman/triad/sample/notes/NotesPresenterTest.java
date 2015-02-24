package com.nhaarman.triad.sample.notes;

import com.nhaarman.triad.sample.MyFlowListener;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.editnote.EditNoteScreen;
import com.nhaarman.triad.sample.notes.noteslist.NotesListContainer;
import com.nhaarman.triad.sample.notes.noteslist.NotesListPresenter;
import flow.Backstack;
import flow.Flow;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotesPresenterTest {

  private NotesPresenter mNotesPresenter;
  private MyFlowListener mFlowListener;

  private NotesListPresenter mNotesListPresenterMock;

  @Before
  public void setUp() {
    mFlowListener = new MyFlowListener();
    Flow flow = new Flow(Backstack.single(new Object()), mFlowListener);

    mNotesListPresenterMock = mock(NotesListPresenter.class);
    mNotesPresenter = new NotesPresenter(flow, mNotesListPresenterMock);
  }

  @Test
  public void onInitialization_aNoteClickedListenerIsRegistered() {
    verify(mNotesListPresenterMock).setNoteClickedListener(any(NotesListPresenter.OnNoteClickedListener.class));
  }

  @Test
  public void onControlGained_setsNotesListPresenterToTheNotesListContainer() {
    /* Given */
    NotesContainer notesContainerMock = mock(NotesContainer.class);
    NotesListContainer notesListContainerMock = mock(NotesListContainer.class);
    when(notesContainerMock.getNotesListContainer()).thenReturn(notesListContainerMock);

    /* When */
    mNotesPresenter.onControlGained(notesContainerMock);

    /* Then */
    verify(notesListContainerMock).setPresenter(mNotesListPresenterMock);
  }

  @Test
  public void onCreateNoteClicked_goesToEditNoteScreen() {
    /* When */
    mNotesPresenter.onCreateNoteClicked();

    /* Then */
    assertThat(mFlowListener.lastScreen, is(instanceOf(EditNoteScreen.class)));
    assertThat(((EditNoteScreen) mFlowListener.lastScreen).getNote(), is(nullValue()));
  }

  @Test
  public void onNoteClicked_goesToEditNoteScreen() {
    /* Given */
    Note note = new Note();

    /* When */
    mNotesPresenter.onNoteClicked(note);

    /* Then */
    assertThat(mFlowListener.lastScreen, is(instanceOf(EditNoteScreen.class)));
    assertThat(((EditNoteScreen) mFlowListener.lastScreen).getNote(), is(note));
  }
}
