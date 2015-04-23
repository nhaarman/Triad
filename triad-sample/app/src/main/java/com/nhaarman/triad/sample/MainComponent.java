package com.nhaarman.triad.sample;

import org.jetbrains.annotations.NotNull;

public class MainComponent {

  @NotNull
  private final NoteRepository mNoteRepository;

  @NotNull
  private final NoteCreator mNoteCreator;

  @NotNull
  private final NoteValidator mNoteValidator;

  public MainComponent() {
    mNoteRepository = new MemoryNoteRepository();
    mNoteCreator = new NoteCreator(mNoteRepository);
    mNoteValidator = new NoteValidator();
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
