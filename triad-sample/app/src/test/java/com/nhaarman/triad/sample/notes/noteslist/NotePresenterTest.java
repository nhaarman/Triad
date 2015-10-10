package com.nhaarman.triad.sample.notes.noteslist;

import com.nhaarman.triad.sample.ActivityComponent;
import com.nhaarman.triad.sample.Note;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NotePresenterTest {

  private static final String TITLE = "title";

  private static final String CONTENTS = "contents";

  private NoteContainer mNoteContainerMock;

  private Note mNote;

  @Before
  public void setUp() {
    mNoteContainerMock = mock(NoteContainer.class);

    mNote = new Note();
    mNote.setTitle(TITLE);
    mNote.setContents(CONTENTS);
  }

  @Test
  public void onControlGained_setsTitleToContainer() {
    /* Given */
    NotePresenter notePresenter = new NotePresenter(mNote);

    /* When */
    notePresenter.acquire(mNoteContainerMock, mock(ActivityComponent.class));

    /* Then */
    verify(mNoteContainerMock).setTitle(TITLE);
  }

  @Test
  public void onControlGained_setsContentsToContainer() {
    /* Given */
    NotePresenter notePresenter = new NotePresenter(mNote);

    /* When */
    notePresenter.acquire(mNoteContainerMock, mock(ActivityComponent.class));

    /* Then */
    verify(mNoteContainerMock).setContents(CONTENTS);
  }
}