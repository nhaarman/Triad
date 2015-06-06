package com.nhaarman.triad.sample.editnote;

import com.nhaarman.triad.ScreenContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EditNoteContainer extends ScreenContainer<EditNotePresenter, EditNoteContainer> {

  void setTitle(@NotNull String title);

  void setContents(@NotNull String contents);

  @NotNull
  String getTitle();

  @NotNull
  String getContents();

  void setTitleError(@Nullable String message);

  void setContentsError(@Nullable String message);
}
