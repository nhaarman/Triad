package com.nhaarman.triad.sample.notes.noteslist;

import com.nhaarman.triad.presenter.Presenter;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.NoteRepository;
import java.util.List;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NotesListPresenter extends Presenter<NotesListPresenter, NotesListContainer> {

  @NotNull
  private final NoteRepository mNoteRepository;

  @Nullable
  private List<Note> mNotes;

  @Nullable
  private OnNoteClickedListener mListener;

  @Inject
  public NotesListPresenter(@NotNull final NoteRepository noteRepository) {
    mNoteRepository = noteRepository;
  }

  @Override
  protected void onControlGained(@NotNull final NotesListContainer container) {
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

  public interface OnNoteClickedListener {

    void onNoteClicked(@NotNull Note note);
  }
}
