package com.nhaarman.triad.sample.notes.noteslist;

import com.nhaarman.triad.sample.ActivityComponent;
import com.nhaarman.triad.sample.MemoryNoteRepository;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.NoteRepository;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class NotesListPresenterTest {

  private NotesListPresenter mNotesListPresenter;

  private NoteRepository mNoteRepository;

  private Note mNote1;

  private Note mNote2;

  private NotesListPresenter.OnNoteClickedListener mOnNoteClickedListener;

  @Before
  public void setUp() {
    mNoteRepository = spy(new MemoryNoteRepository());
    mOnNoteClickedListener = mock(NotesListPresenter.OnNoteClickedListener.class);
    mNotesListPresenter = new NotesListPresenter(mNoteRepository, mOnNoteClickedListener);

    mNote1 = new Note();
    mNoteRepository.create(mNote1);

    mNote2 = new Note();
    mNoteRepository.create(mNote2);

    reset(mNoteRepository);
  }

  @Test
  public void onControlGained_requestsAllNotes() {
    /* When */
    mNotesListPresenter.onControlGained(mock(NotesListContainer.class), new ActivityComponent());

    /* Then */
    verify(mNoteRepository).findAll();
  }

  @Test
  public void onControlGained_setsNotePresentersToContainer() {
    /* Given */
    NotesListContainer containerMock = mock(NotesListContainer.class);

    /* When */
    mNotesListPresenter.onControlGained(containerMock, new ActivityComponent());

    /* Then */
    ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
    verify(containerMock).setNotes((List<NotePresenter>) captor.capture());

    List<NotePresenter> notePresenters = captor.getValue();
    assertThat(notePresenters.size(), is(2));
  }

  @Test
  public void onNoteClicked_notifiesListener() {
    /* Given */
    mNotesListPresenter.acquire(mock(NotesListContainer.class), mock(ActivityComponent.class));

    /* When */
    mNotesListPresenter.onNoteClicked(0);

    /* Then */
    verify(mOnNoteClickedListener).onNoteClicked(mNote1);
  }
}