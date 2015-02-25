package com.nhaarman.triad.sample.notes;

import com.nhaarman.triad.sample.MainComponent;
import com.nhaarman.triad.sample.R;
import com.nhaarman.triad.sample.notes.noteslist.NotesListPresenter;
import com.nhaarman.triad.screen.Screen;
import dagger.Component;
import org.jetbrains.annotations.NotNull;

public class NotesScreen extends Screen<NotesPresenter, NotesContainer, MainComponent> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_notes;
  }

  @NotNull
  @Override
  protected NotesPresenter createPresenter(@NotNull final MainComponent mainComponent) {
    return Dagger_NotesScreen_NotesComponent.builder().mainComponent(mainComponent).build().notesPresenter();
  }


  @NotesScope
  @Component(dependencies = MainComponent.class)
  interface NotesComponent {

    NotesPresenter notesPresenter();

  }

}
