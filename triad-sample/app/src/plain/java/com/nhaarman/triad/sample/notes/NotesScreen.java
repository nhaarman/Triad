package com.nhaarman.triad.sample.notes;

import com.nhaarman.triad.sample.MainComponent;
import com.nhaarman.triad.sample.R;
import com.nhaarman.triad.sample.notes.noteslist.NotesListPresenter;
import com.nhaarman.triad.screen.Screen;
import org.jetbrains.annotations.NotNull;

public class NotesScreen extends Screen<NotesPresenter, NotesContainer, MainComponent> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_notes;
  }

  @NotNull
  @Override
  protected NotesPresenter createPresenter(@NotNull final MainComponent mainComponent) {
    NotesListPresenter notesListPresenter = new NotesListPresenter(mainComponent.noteRepository());
    return new NotesPresenter(mainComponent.flow(), notesListPresenter);
  }
}
