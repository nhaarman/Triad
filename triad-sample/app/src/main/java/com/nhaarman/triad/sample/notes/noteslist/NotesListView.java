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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.OnItemClick;
import com.nhaarman.triad.RelativeLayoutContainer;
import com.nhaarman.triad.sample.ActivityComponent;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.R;
import java.util.List;

public class NotesListView extends RelativeLayoutContainer<ActivityComponent, NotesListPresenter, NotesListContainer> implements NotesListContainer {

  @NonNull
  private final MyAdapter mAdapter;

  @Bind(R.id.view_notes_listview)
  protected ListView mListView;

  @Nullable
  private List<Note> mNotes;

  public NotesListView(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public NotesListView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle, NotesListPresenter.class);
    mAdapter = new MyAdapter();
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    if (!isInEditMode()) {
      mListView.setAdapter(mAdapter);
    }
  }

  @Override
  public void setNotes(@NonNull final List<Note> notes) {
    mNotes = notes;
    mAdapter.notifyDataSetChanged();
  }

  @OnItemClick(R.id.view_notes_listview)
  public void onItemClicked(final int position) {
    getPresenter().onNoteClicked(position);
  }

  private class MyAdapter extends BaseAdapter {

    @Override
    public int getCount() {
      return mNotes == null ? 0 : mNotes.size();
    }

    @Override
    public Note getItem(final int position) {
      return mNotes == null ? null : mNotes.get(position);
    }

    @Override
    public long getItemId(final int position) {
      return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
      NoteContainer noteContainer = (NoteContainer) convertView;
      if (noteContainer == null) {
        noteContainer = (NoteContainer) LayoutInflater.from(getContext()).inflate(R.layout.view_note, parent, false);
      }

      noteContainer.getPresenter().setNote(getItem(position));

      return (View) noteContainer;
    }
  }
}

