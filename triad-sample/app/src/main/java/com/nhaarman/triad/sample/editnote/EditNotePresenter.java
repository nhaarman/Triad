package com.nhaarman.triad.sample.editnote;

import com.nhaarman.triad.presenter.ScreenPresenter;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.NoteCreator;
import com.nhaarman.triad.sample.NoteRepository;
import com.nhaarman.triad.sample.NoteValidator;
import com.nhaarman.triad.sample.R;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditNotePresenter extends ScreenPresenter<EditNotePresenter, EditNoteContainer> {

  @Nullable
  private final Note mNote;

  @NotNull
  private final NoteValidator mNoteValidator;

  @NotNull
  private final NoteCreator mNoteCreatorMock;

  @NotNull
  private final NoteRepository mNoteRepository;


  public EditNotePresenter(@Nullable final Note note,
                           @NotNull final NoteValidator noteValidator,
                           @NotNull final NoteCreator noteCreatorMock,
                           @NotNull final NoteRepository noteRepository) {
    mNote = note;
    mNoteValidator = noteValidator;
    mNoteCreatorMock = noteCreatorMock;
    mNoteRepository = noteRepository;
  }

  @Override
  protected void onControlGained(@NotNull final EditNoteContainer container) {
    String title = "";
    String contents = "";

    if (mNote != null) {
      title = mNote.getTitle();
      contents = mNote.getContents();
    }

    container.setTitle(title);
    container.setContents(contents);
  }

  public void onSaveNoteClicked() {
    if (getContainer() == null) {
      return;
    }

    String title = getContainer().getTitle();
    String contents = getContainer().getContents();

    if (!mNoteValidator.validateTitle(title)) {
      getContainer().setTitleError(getString(R.string.error_title));
      return;
    }

    if (!mNoteValidator.validateContents(contents)) {
      getContainer().setContentsError(getString(R.string.error_contents));
      return;
    }

    if (mNote == null) {
      mNoteCreatorMock.createNote(title, contents);
    } else {
      mNoteRepository.update(mNote);
    }

    getFlow().goBack();
  }
}
