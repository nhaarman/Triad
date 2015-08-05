package com.nhaarman.triad.sample.notes.noteslist;

import com.nhaarman.triad.sample.MemoryNoteRepository;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.NoteRepository;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NotesListPresenterTest {

  private NotesListPresenter mNotesListPresenter;

  private NoteRepository mNoteRepository;

  private Note mNote1;

  private Note mNote2;

  @Before
  public void setUp() {
    mNoteRepository = spy(new MemoryNoteRepository());
    mNotesListPresenter = new NotesListPresenter(mNoteRepository);

    mNote1 = new Note();
    mNoteRepository.create(mNote1);

    mNote2 = new Note();
    mNoteRepository.create(mNote2);

    reset(mNoteRepository);
  }

  @Test
  public void onControlGained_requestsAllNotes() {
    /* When */
    mNotesListPresenter.onControlGained(mock(NotesListContainer.class));

    /* Then */
    verify(mNoteRepository).findAll();
  }

  @Test
  public void onControlGained_setsNotesToContainer() {
    /* Given */
    NotesListContainer containerMock = mock(NotesListContainer.class);

    /* When */
    mNotesListPresenter.onControlGained(containerMock);

    /* Then */
    verify(containerMock).setNotes((List<Note>) argThat(contains(mNote1, mNote2)));
  }

  @Test
  public void onNoteClicked_notifiesListener() {
    /* Given */
    NotesListPresenter.OnNoteClickedListener listenerMock = mock(NotesListPresenter.OnNoteClickedListener.class);
    mNotesListPresenter.setNoteClickedListener(listenerMock);
    mNotesListPresenter.acquire(mock(NotesListContainer.class));

    /* When */
    mNotesListPresenter.onNoteClicked(0);

    /* Then */
    verify(listenerMock).onNoteClicked(mNote1);
  }
}