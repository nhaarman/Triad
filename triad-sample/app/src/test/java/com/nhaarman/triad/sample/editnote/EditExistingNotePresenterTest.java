package com.nhaarman.triad.sample.editnote;

import android.content.Context;
import com.nhaarman.triad.Triad;
import com.nhaarman.triad.sample.MemoryNoteRepository;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.NoteCreator;
import com.nhaarman.triad.sample.NoteRepository;
import com.nhaarman.triad.sample.NoteValidator;
import com.nhaarman.triad.sample.notes.NotesScreen;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditExistingNotePresenterTest {

  private static final String TITLE = "title";
  private static final String CONTENTS = "contents";

  private EditNotePresenter mEditNotePresenter;
  private EditNoteContainer mEditNoteContainerMock;

  private Note mNote;
  private NoteRepository mNoteRepository;
  private NoteValidator mNoteValidator;

  private Triad mTriad;

  @Before
  public void setUp() {
    mNote = new Note();
    mNote.setTitle(TITLE);
    mNote.setContents(CONTENTS);

    mNoteValidator = spy(new NoteValidator());
    mNoteRepository = spy(new MemoryNoteRepository());

    mTriad = mock(Triad.class);

    mEditNotePresenter = new EditNotePresenter(mNote, mNoteValidator, mock(NoteCreator.class), mNoteRepository);
    mEditNotePresenter.setTriad(mTriad);

    mEditNoteContainerMock = mock(EditNoteContainer.class);
    when(mEditNoteContainerMock.getContext()).thenReturn(mock(Context.class));
  }

  @Test
  public void onControlGained_setsNoteTitleToEditNoteContainer() {
    /* Given */
    EditNoteContainer containerMock = mock(EditNoteContainer.class);

    /* When */
    mEditNotePresenter.onControlGained(containerMock);

    /* Then */
    verify(containerMock).setTitle(TITLE);
  }

  @Test
  public void onControlGained_setsNoteContentsToEditNoteContainer() {
    /* Given */
    EditNoteContainer containerMock = mock(EditNoteContainer.class);

    /* When */
    mEditNotePresenter.onControlGained(containerMock);

    /* Then */
    verify(containerMock).setContents(CONTENTS);
  }

  @Test
  public void onSaveNoteClicked_validatesTitle() {
    /* Given */
    mEditNotePresenter.acquire(mEditNoteContainerMock);

    when(mEditNoteContainerMock.getTitle()).thenReturn(TITLE);
    when(mEditNoteContainerMock.getContents()).thenReturn(CONTENTS);

    /* When */
    mEditNotePresenter.onSaveNoteClicked();

    /* Then */
    verify(mNoteValidator).validateTitle(TITLE);
  }

  @Test
  public void onSaveNoteClicked_validatesContents() {
    /* Given */
    mEditNotePresenter.acquire(mEditNoteContainerMock);

    when(mEditNoteContainerMock.getTitle()).thenReturn(TITLE);
    when(mEditNoteContainerMock.getContents()).thenReturn(CONTENTS);

    /* When */
    mEditNotePresenter.onSaveNoteClicked();

    /* Then */
    verify(mNoteValidator).validateContents(CONTENTS);
  }

  @Test
  public void onSaveNoteClickedWithEmptyTitle_showsErrorMessage() {
    /* Given */
    mEditNotePresenter.acquire(mEditNoteContainerMock);

    when(mEditNoteContainerMock.getTitle()).thenReturn("");
    when(mEditNoteContainerMock.getContents()).thenReturn(CONTENTS);

    /* When */
    mEditNotePresenter.onSaveNoteClicked();

    /* Then */
    verify(mEditNoteContainerMock).setTitleError(any(String.class));
  }

  @Test
  public void onSaveNoteClickedWithEmptyContents_showsErrorMessage() {
    /* Given */
    mEditNotePresenter.acquire(mEditNoteContainerMock);

    when(mEditNoteContainerMock.getTitle()).thenReturn(TITLE);
    when(mEditNoteContainerMock.getContents()).thenReturn("");

    /* When */
    mEditNotePresenter.onSaveNoteClicked();

    /* Then */
    verify(mEditNoteContainerMock).setContentsError(any(String.class));
  }

  @Test
  public void onSaveNoteClicked_persistsTheNote() {
    /* Given */
    mEditNotePresenter.acquire(mEditNoteContainerMock);

    when(mEditNoteContainerMock.getTitle()).thenReturn(TITLE);
    when(mEditNoteContainerMock.getContents()).thenReturn(CONTENTS);

    /* When */
    mEditNotePresenter.onSaveNoteClicked();

    /* Then */
    verify(mNoteRepository).update(mNote);
  }

  @Test
  public void onSaveNoteClicked_navigatesBack() {
    /* Given */
    EditNoteContainer containerMock = mock(EditNoteContainer.class);
    mEditNotePresenter.acquire(containerMock);

    when(containerMock.getTitle()).thenReturn(TITLE);
    when(containerMock.getContents()).thenReturn(CONTENTS);

    /* When */
    mEditNotePresenter.onSaveNoteClicked();

    /* Then */
    verify(mTriad).goBack();
  }
}