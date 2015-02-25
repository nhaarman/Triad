package com.nhaarman.triad.sample;

import dagger.Module;
import dagger.Provides;
import flow.Flow;
import org.jetbrains.annotations.NotNull;

@Module
public class MainModule {

  @NotNull
  private final Flow mFlow;

  public MainModule(@NotNull final Flow flow) {
    mFlow = flow;
  }

  @Provides
  Flow flow() {
    return mFlow;
  }

  @MainScope
  @Provides
  NoteRepository noteRepository(final MemoryNoteRepository noteRepository) {
    return noteRepository;
  }
}
