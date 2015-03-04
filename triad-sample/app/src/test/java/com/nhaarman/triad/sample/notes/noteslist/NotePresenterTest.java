package com.nhaarman.triad.sample.notes.noteslist;

import com.nhaarman.triad.sample.Note;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NotePresenterTest {

  private static final String TITLE = "title";
  private static final String CONTENTS = "contents";

  private NotePresenter mNotePresenter;
  private NoteContainer mNoteContainerMock;
  private Note mNote;

  @Before
  public void setUp() {
    mNotePresenter = new NotePresenter();

    mNoteContainerMock = mock(NoteContainer.class);
    mNotePresenter.acquire(mNoteContainerMock);

    mNote = new Note();
    mNote.setTitle(TITLE);
    mNote.setContents(CONTENTS);
  }

  @Test
  public void settingANote_setsTitleToContainer() {
    /* When */
    mNotePresenter.setNote(mNote);

    /* Then */
    verify(mNoteContainerMock).setTitle(TITLE);
  }

  @Test
  public void settingANote_setsContentsToContainer() {
    /* When */
    mNotePresenter.setNote(mNote);

    /* Then */
    verify(mNoteContainerMock).setContents(CONTENTS);
  }

  @Test
  public void settingANoteBeforeAcquiringContainer_doesNotCrash() {
    /* Given */
    NotePresenter notePresenter = new NotePresenter();

    /* When */
    notePresenter.setNote(mNote);

    /* Then */
    assertThat(true, is(true));
  }

  @Test
  public void settingANote_andThenAcquiringTheContainer_setsTitleToContainer() {
    /* Given */
    NotePresenter notePresenter = new NotePresenter();
    notePresenter.setNote(mNote);

    /* When */
    notePresenter.acquire(mNoteContainerMock);

    /* Then */
    verify(mNoteContainerMock).setTitle(TITLE);
  }

  @Test
  public void settingANote_andThenAcquiringTheContainer_setsContentsToContainer() {
    /* Given */
    NotePresenter notePresenter = new NotePresenter();
    notePresenter.setNote(mNote);

    /* When */
    notePresenter.acquire(mNoteContainerMock);

    /* Then */
    verify(mNoteContainerMock).setContents(CONTENTS);
  }
}