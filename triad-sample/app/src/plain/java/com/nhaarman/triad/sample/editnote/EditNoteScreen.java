package com.nhaarman.triad.sample.editnote;

import com.nhaarman.triad.sample.MainComponent;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.R;
import com.nhaarman.triad.screen.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditNoteScreen extends Screen<EditNotePresenter, EditNoteContainer, MainComponent> {

  @Nullable
  private final Note mNote;

  public EditNoteScreen() {
    this(null);
  }

  public EditNoteScreen(@Nullable final Note note) {
    mNote = note;
  }

  @Nullable
  public Note getNote() {
    return mNote;
  }

  @Override
  public boolean isDialog() {
    return true;
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.view_editnote;
  }

  @NotNull
  @Override
  protected EditNotePresenter createPresenter(@NotNull final MainComponent mainComponent) {
    return new EditNotePresenter(mNote,
        mainComponent.noteValidator(),
        mainComponent.noteCreator(),
        mainComponent.noteRepository(),
        mainComponent.flow()
    );
  }
}
