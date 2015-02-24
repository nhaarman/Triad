package com.nhaarman.triad.sample.editnote;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.EditText;
import butterknife.InjectView;
import butterknife.OnClick;
import com.nhaarman.triad.container.RelativeLayoutContainer;
import com.nhaarman.triad.sample.R;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditNoteView extends RelativeLayoutContainer<EditNotePresenter, EditNoteContainer> implements EditNoteContainer {

  @NotNull
  @InjectView(R.id.view_editnote_titleet)
  protected EditText mTitleET;

  @NotNull
  @InjectView(R.id.view_editnote_contentset)
  protected EditText mContentsET;

  public EditNoteView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public EditNoteView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  public EditNoteView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @OnClick(R.id.view_editnote_savebutton)
  public void onSaveButtonClicked() {
    getPresenter().onSaveNoteClicked();
  }

  @NotNull
  @Override
  public String getTitle() {
    return mTitleET.getText().toString();
  }

  @Override
  public void setTitle(@NotNull final String title) {
    mTitleET.setText(title);
  }

  @NotNull
  @Override
  public String getContents() {
    return mContentsET.getText().toString();
  }

  @Override
  public void setContents(@NotNull final String contents) {
    mContentsET.setText(contents);
  }

  @Override
  public void setTitleError(@Nullable final String message) {
    mTitleET.setHint(message);
    mTitleET.setHintTextColor(Color.RED);
  }

  @Override
  public void setContentsError(@Nullable final String message) {
    mContentsET.setHint(message);
    mContentsET.setHintTextColor(Color.RED);
  }
}
