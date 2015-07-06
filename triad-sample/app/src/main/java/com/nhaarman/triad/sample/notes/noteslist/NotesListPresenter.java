/*
 * Copyright 2015 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.triad.sample.notes.noteslist;

import com.nhaarman.triad.Presenter;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.NoteRepository;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NotesListPresenter extends Presenter<NotesListPresenter, NotesListContainer> {

  @NotNull
  private final NoteRepository mNoteRepository;

  @Nullable
  private List<Note> mNotes;

  @Nullable
  private OnNoteClickedListener mListener;

  public NotesListPresenter(@NotNull final NoteRepository noteRepository) {
    mNoteRepository = noteRepository;
  }

  @Override
  protected void onControlGained(@NotNull final NotesListContainer container) {
    mNotes = mNoteRepository.findAll();
    container.setNotes(mNotes);
  }

  public void setNoteClickedListener(final OnNoteClickedListener listener) {
    mListener = listener;
  }

  public void onNoteClicked(final int position) {
    if (mNotes == null || mListener == null) {
      return;
    }

    mListener.onNoteClicked(mNotes.get(position));
  }

  public interface OnNoteClickedListener {

    void onNoteClicked(@NotNull Note note);
  }
}
