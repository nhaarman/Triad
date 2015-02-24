package com.nhaarman.triad.sample.notes;

import android.content.Context;
import android.util.AttributeSet;
import butterknife.InjectView;
import butterknife.OnClick;
import com.nhaarman.triad.container.RelativeLayoutContainer;
import com.nhaarman.triad.sample.R;
import com.nhaarman.triad.sample.notes.noteslist.NotesListContainer;
import com.nhaarman.triad.sample.notes.noteslist.NotesListView;
import org.jetbrains.annotations.NotNull;

public class NotesView extends RelativeLayoutContainer<NotesPresenter, NotesContainer> implements NotesContainer {

  @NotNull
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
  @NotNull
  public NotesListContainer getNotesListContainer() {
    return mNotesListView;
  }

  @OnClick(R.id.view_notes_createnotebutton)
  public void onCreateNoteButtonClicked() {
    getPresenter().onCreateNoteClicked();
  }
}
