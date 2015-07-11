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

package com.nhaarman.triad.sample.notes;

import com.nhaarman.triad.ScreenPresenter;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.editnote.EditNoteScreen;
import com.nhaarman.triad.sample.notes.noteslist.NotesListPresenter;
import android.support.annotation.NonNull;

class NotesPresenter extends ScreenPresenter<NotesPresenter, NotesContainer> implements NotesListPresenter.OnNoteClickedListener {

  @NonNull
  private final NotesListPresenter mNotesListPresenter;

  NotesPresenter(@NonNull final NotesListPresenter notesListPresenter) {
    mNotesListPresenter = notesListPresenter;
    mNotesListPresenter.setNoteClickedListener(this);
  }

  @Override
  protected void onControlGained(@NonNull final NotesContainer container) {
    container.getNotesListContainer().setPresenter(mNotesListPresenter);
  }

  public void onCreateNoteClicked() {
    getTriad().goTo(new EditNoteScreen());
  }

  @Override
  public void onNoteClicked(@NonNull final Note note) {
    getTriad().goTo(new EditNoteScreen(note));
  }
}
