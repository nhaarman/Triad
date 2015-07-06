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

import com.nhaarman.triad.Screen;
import com.nhaarman.triad.sample.MainComponent;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.R;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditNoteScreen extends Screen<EditNotePresenter, EditNoteContainer, MainComponent> {

  @Nullable
  private final Note mNote;

  public EditNoteScreen() {
    this(null);
  }

  public EditNoteScreen(@Nullable final Note note) {
    mNote = note;
  }

  @Nullable
  public Note getNote() {
    return mNote;
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.view_editnote;
  }

  @NotNull
  @Override
  protected EditNotePresenter createPresenter(@NotNull final MainComponent mainComponent) {
    return new EditNotePresenter(mNote,
        mainComponent.noteValidator(),
        mainComponent.noteCreator(),
        mainComponent.noteRepository()
    );
  }
}
