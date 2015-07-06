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

package com.nhaarman.triad.sample.editnote;

import com.nhaarman.triad.Consumer;
import com.nhaarman.triad.ScreenPresenter;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.NoteCreator;
import com.nhaarman.triad.sample.NoteRepository;
import com.nhaarman.triad.sample.NoteValidator;
import com.nhaarman.triad.sample.R;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditNotePresenter extends ScreenPresenter<EditNotePresenter, EditNoteContainer> {

  @Nullable
  private final Note mNote;

  @NotNull
  private final NoteValidator mNoteValidator;

  @NotNull
  private final NoteCreator mNoteCreatorMock;

  @NotNull
  private final NoteRepository mNoteRepository;

  public EditNotePresenter(@Nullable final Note note,
                           @NotNull final NoteValidator noteValidator,
                           @NotNull final NoteCreator noteCreatorMock,
                           @NotNull final NoteRepository noteRepository) {
    mNote = note;
    mNoteValidator = noteValidator;
    mNoteCreatorMock = noteCreatorMock;
    mNoteRepository = noteRepository;
  }

  @Override
  protected void onControlGained(@NotNull final EditNoteContainer container) {
    String title = "";
    String contents = "";

    if (mNote != null) {
      title = mNote.getTitle();
      contents = mNote.getContents();
    }

    container.setTitle(title);
    container.setContents(contents);
  }

  public void onSaveNoteClicked() {
    //noinspection AnonymousInnerClass
    getContainer().ifPresent(new Consumer<EditNoteContainer>() {
      @Override
      public void accept(@NotNull final EditNoteContainer container) {
        String title = container.getTitle();
        String contents = container.getContents();

        if (!mNoteValidator.validateTitle(title)) {
          container.setTitleError(getString(R.string.error_title));
          return;
        }

        if (!mNoteValidator.validateContents(contents)) {
          container.setContentsError(getString(R.string.error_contents));
          return;
        }

        if (mNote == null) {
          mNoteCreatorMock.createNote(title, contents);
        } else {
          mNoteRepository.update(mNote);
        }

        getTriad().goBack();
      }
    });
  }
}
