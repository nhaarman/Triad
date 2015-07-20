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

import android.support.annotation.NonNull;
import com.nhaarman.triad.Screen;
import com.nhaarman.triad.sample.MainComponent;
import com.nhaarman.triad.sample.R;
import com.nhaarman.triad.sample.notes.noteslist.NotesListPresenter;

public class NotesScreen extends Screen<NotesPresenter, NotesContainer, MainComponent> {

  @Override
  protected int getLayoutResId() {
    return R.layout.view_notes;
  }

  @NonNull
  @Override
  protected NotesPresenter createPresenter(@NonNull final MainComponent mainComponent) {
    NotesListPresenter notesListPresenter = new NotesListPresenter(mainComponent.noteRepository());
    return new NotesPresenter(notesListPresenter);
  }
}
