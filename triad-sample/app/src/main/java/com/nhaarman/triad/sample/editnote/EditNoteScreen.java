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

package com.nhaarman.triad.sample.editnote;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.nhaarman.triad.Triad;
import com.nhaarman.triad.sample.ApplicationComponent;
import com.nhaarman.triad.sample.Note;
import com.nhaarman.triad.sample.R;
import com.nhaarman.triad.sample.SampleScreen;

public class EditNoteScreen extends SampleScreen<EditNotePresenter, EditNoteContainer> {

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

  @NonNull
  @Override
  protected EditNotePresenter createPresenter(@NonNull final ApplicationComponent applicationComponent) {
    return new EditNotePresenter(
        mNote,
        applicationComponent.noteValidator(),
        applicationComponent.noteCreator(),
        applicationComponent.noteRepository()
    );
  }

  @Override
  public boolean animateTransition(@Nullable final View oldView,
                                   @NonNull final View newView,
                                   @NonNull final Triad.Direction direction,
                                   @NonNull final Triad.Callback callback) {
    assert oldView != null;

    oldView.animate().x(-oldView.getWidth()).setInterpolator(new AccelerateInterpolator());
    newView.setX(((View) newView.getParent()).getWidth());
    newView.animate().x(0).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(final Animator animation) {
        newView.animate().setListener(null);
        ((ViewManager) oldView.getParent()).removeView(oldView);

        callback.onComplete();
      }
    });

    return true;
  }
}
