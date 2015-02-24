package com.nhaarman.triad.sample;

import flow.Flow;
import org.jetbrains.annotations.NotNull;

public class MainComponent {

  @NotNull
  private final Flow mFlow;

  @NotNull
  private final NoteRepository mNoteRepository;

  @NotNull
  private final NoteCreator mNoteCreator;

  @NotNull
  private final NoteValidator mNoteValidator;

  public MainComponent(@NotNull final Flow flow) {
    mFlow = flow;
    mNoteRepository = new MemoryNoteRepository();
    mNoteCreator = new NoteCreator(mNoteRepository);
    mNoteValidator = new NoteValidator();
  }

  @NotNull
  public Flow flow() {
    return mFlow;
  }

  @NotNull
  public NoteRepository noteRepository() {
    return mNoteRepository;
  }

  @NotNull
  public NoteCreator noteCreator() {
    return mNoteCreator;
  }

  public NoteValidator noteValidator() {
    return mNoteValidator;
  }
}
