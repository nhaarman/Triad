package com.nhaarman.triad.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MemoryNoteRepository implements NoteRepository {

  @NotNull
  private final Map<Long, Note> mNotes;

  private long mLastId;

  @Inject
  public MemoryNoteRepository() {
    mNotes = new HashMap<>(16);
    mLastId = 0;
  }

  @Override
  public Long create(@NotNull final Note note) {
    mLastId++;
    note.setCreated(System.currentTimeMillis());
    mNotes.put(mLastId, note);
    return mLastId;
  }

  @Nullable
  @Override
  public Note find(@Nullable final Long id) {
    if (id == null) {
      return null;
    }

    return mNotes.get(id);
  }

  @NotNull
  @Override
  public List<Note> findAll() {
    return new ArrayList<>(mNotes.values());
  }

  @Override
  public boolean update(@NotNull final Note note) {
    return mNotes.values().contains(note);
  }
}
