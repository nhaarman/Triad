package com.nhaarman.triad.sample;

import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

public class NoteCreator {

  @NotNull
  private final NoteRepository mNoteRepository;

  @Inject
  public NoteCreator(@NotNull final NoteRepository noteRepository) {
    mNoteRepository = noteRepository;
  }

  @NotNull
  public Note createNote(final String title, final String contents) {
    Note note = new Note();

    note.setTitle(title);
    note.setContents(contents);
    note.setCreated(System.currentTimeMillis());

    mNoteRepository.create(note);

    return note;
  }
}
