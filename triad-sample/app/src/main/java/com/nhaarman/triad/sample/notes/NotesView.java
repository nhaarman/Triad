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

import android.content.Context;
import android.util.AttributeSet;
import butterknife.InjectView;
import butterknife.OnClick;
import com.nhaarman.triad.RelativeLayoutContainer;
import com.nhaarman.triad.sample.R;
import com.nhaarman.triad.sample.notes.noteslist.NotesListContainer;
import com.nhaarman.triad.sample.notes.noteslist.NotesListView;
import android.support.annotation.NonNull;

public class NotesView extends RelativeLayoutContainer<NotesPresenter, NotesContainer> implements NotesContainer {

  @NonNull
  @InjectView(R.id.view_notes_noteslistview)
  protected NotesListView mNotesListView;

  public NotesView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public NotesView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  public NotesView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  @NonNull
  public NotesListContainer getNotesListContainer() {
    return mNotesListView;
  }

  @OnClick(R.id.view_notes_createnotebutton)
  public void onCreateNoteButtonClicked() {
    getPresenter().onCreateNoteClicked();
  }
}
