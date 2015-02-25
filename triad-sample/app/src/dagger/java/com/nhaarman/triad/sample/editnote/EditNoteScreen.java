package com.nhaarman.triad.sample.editnote;

import com.nhaarman.triad.sample.MainComponent;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.R;
import com.nhaarman.triad.screen.Screen;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
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
  protected int getLayoutResId() {
    return R.layout.view_editnote;
  }

  @NotNull
  @Override
  protected EditNotePresenter createPresenter(@NotNull final MainComponent mainComponent) {
    return Dagger_EditNoteScreen_EditNoteComponent.builder()
        .editNoteModule(new EditNoteModule())
        .mainComponent(mainComponent)
        .build()
        .editNotePresenter();
  }

  @EditNoteScope
  @Component(modules = EditNoteModule.class, dependencies = MainComponent.class)
  interface EditNoteComponent {

    EditNotePresenter editNotePresenter();
  }

  @Module
  class EditNoteModule {

    @Provides
    @Nullable
    Note note() {
      return mNote;
    }
  }
}
