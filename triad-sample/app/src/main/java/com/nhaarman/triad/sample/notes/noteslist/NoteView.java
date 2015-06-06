package com.nhaarman.triad.sample.notes.noteslist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import butterknife.InjectView;
import com.nhaarman.triad.RelativeLayoutContainer;
import com.nhaarman.triad.sample.R;
import org.jetbrains.annotations.NotNull;

public class NoteView extends RelativeLayoutContainer<NotePresenter, NoteContainer> implements NoteContainer {

  @NotNull
  @InjectView(R.id.view_note_titletv)
  protected TextView mTitleTV;

  @NotNull
  @InjectView(R.id.view_note_contentstv)
  protected TextView mContentsTV;

  public NoteView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public NoteView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public void setTitle(@NotNull final String title) {
    mTitleTV.setText(title);
  }

  @Override
  public void setContents(@NotNull final String contents) {
    mContentsTV.setText(contents);
  }
}
