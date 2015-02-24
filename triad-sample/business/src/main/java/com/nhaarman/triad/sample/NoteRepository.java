package com.nhaarman.triad.sample;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NoteRepository {

  @Nullable
  Long create(@NotNull final Note note);

  @Nullable
  Note find(@Nullable Long id);

  @NotNull
  List<Note> findAll();

  boolean update(@NotNull final Note note);
}
