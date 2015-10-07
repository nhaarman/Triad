/*
 * Copyright 2015 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.triad.sample.notes.noteslist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.nhaarman.triad.Presenter;
import com.nhaarman.triad.sample.ActivityComponent;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.NoteRepository;
import java.util.List;

public class NotesListPresenter extends Presenter<ActivityComponent, NotesListContainer> {

  @NonNull
  private final NoteRepository mNoteRepository;

  @Nullable
  private List<Note> mNotes;

  @Nullable
  private OnNoteClickedListener mListener;

  public NotesListPresenter(@NonNull final NoteRepository noteRepository) {
    mNoteRepository = noteRepository;
  }

  @Override
  protected void onControlGained(@NonNull final NotesListContainer container, @NonNull final ActivityComponent activityComponent) {
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

  public NotePresenter createNotePresenter() {
    return new NotePresenter();
  }

  public interface OnNoteClickedListener {

    void onNoteClicked(@NonNull Note note);
  }
}
