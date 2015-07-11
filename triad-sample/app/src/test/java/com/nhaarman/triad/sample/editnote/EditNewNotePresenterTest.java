package com.nhaarman.triad.sample.editnote;

import android.content.Context;
import android.content.res.Resources;
import com.nhaarman.triad.Optional;
import com.nhaarman.triad.Screen;
import com.nhaarman.triad.Triad;
import com.nhaarman.triad.sample.MemoryNoteRepository;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.NoteCreator;
import com.nhaarman.triad.sample.NoteValidator;
import com.nhaarman.triad.sample.notes.NotesScreen;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditNewNotePresenterTest {

  private static final String TITLE = "title";

  private static final String CONTENTS = "contents";

  private EditNotePresenter mEditNotePresenter;

  private EditNoteContainer mEditNoteContainerMock;

  private NoteValidator mNoteValidator;

  private NoteCreator mNoteCreatorMock;

  private MemoryNoteRepository mNoteRepository;

  private Triad mTriad;

  @Before
  public void setUp() {
    mNoteRepository = spy(new MemoryNoteRepository());
    mNoteValidator = spy(new NoteValidator());
    mNoteCreatorMock = spy(new NoteCreator(mNoteRepository));

    mTriad = mock(Triad.class);

    mEditNotePresenter = spy(new EditNotePresenter(null, mNoteValidator, mNoteCreatorMock, mNoteRepository));
    mEditNotePresenter.setTriad(mTriad);
    doReturn(Optional.of(mock(Resources.class))).when(mEditNotePresenter).getResources();

    mEditNoteContainerMock = mock(EditNoteContainer.class);
    when(mEditNoteContainerMock.getContext()).thenReturn(mock(Context.class));
  }

  @Test
  public void onControlGained_setsEmptyTitleToEditNoteContainer() {
    /* Given */
    EditNoteContainer containerMock = mock(EditNoteContainer.class);

    /* When */
    mEditNotePresenter.onControlGained(containerMock);

    /* Then */
    verify(containerMock).setTitle("");
  }

  @Test
  public void onControlGained_setsEmptyContentsToEditNoteContainer() {
    /* Given */
    EditNoteContainer containerMock = mock(EditNoteContainer.class);

    /* When */
    mEditNotePresenter.onControlGained(containerMock);

    /* Then */
    verify(containerMock).setContents("");
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
  public void onSaveNoteClickedWithInvalidProperties_staysOnEditNoteScreen() {
    /* Given */
    mEditNotePresenter.acquire(mEditNoteContainerMock);

    when(mEditNoteContainerMock.getTitle()).thenReturn(TITLE);
    when(mEditNoteContainerMock.getContents()).thenReturn("");

    /* When */
    mEditNotePresenter.onSaveNoteClicked();

    /* Then */
    verify(mTriad, never()).goTo(any(Screen.class));
  }

  @Test
  public void onSaveNoteClicked_createsANoteWithTheProperTitle() {
    /* Given */
    mEditNotePresenter.acquire(mEditNoteContainerMock);

    when(mEditNoteContainerMock.getTitle()).thenReturn(TITLE);
    when(mEditNoteContainerMock.getContents()).thenReturn(CONTENTS);

    /* When */
    mEditNotePresenter.onSaveNoteClicked();

    /* Then */
    verify(mNoteCreatorMock).createNote(eq(TITLE), any(String.class));
  }

  @Test
  public void onSaveNoteClicked_createsANoteWithTheProperContents() {
    /* Given */
    mEditNotePresenter.acquire(mEditNoteContainerMock);

    when(mEditNoteContainerMock.getTitle()).thenReturn(TITLE);
    when(mEditNoteContainerMock.getContents()).thenReturn(CONTENTS);

    /* When */
    mEditNotePresenter.onSaveNoteClicked();

    /* Then */
    verify(mNoteCreatorMock).createNote(any(String.class), eq(CONTENTS));
  }

  @Test
  public void onSaveNoteClicked_persistsTheCreatedNote() {
    /* Given */
    mEditNotePresenter.acquire(mEditNoteContainerMock);

    when(mEditNoteContainerMock.getTitle()).thenReturn(TITLE);
    when(mEditNoteContainerMock.getContents()).thenReturn(CONTENTS);

    /* When */
    mEditNotePresenter.onSaveNoteClicked();

    /* Then */
    verify(mNoteRepository).create(any(Note.class));
  }

  @Test
  public void onSaveNoteClicked_navigatesBack() {
    /* Given */
    mEditNotePresenter.acquire(mEditNoteContainerMock);

    when(mEditNoteContainerMock.getTitle()).thenReturn(TITLE);
    when(mEditNoteContainerMock.getContents()).thenReturn(CONTENTS);

    /* When */
    mEditNotePresenter.onSaveNoteClicked();

    /* Then */
    verify(mTriad).goBack();
  }
}