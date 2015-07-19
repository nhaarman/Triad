package com.nhaarman.triad;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public interface TransitionAnimator {

  boolean animateTransition(@Nullable View oldView, @NonNull View newView, @NonNull Triad.Direction direction, @NonNull Triad.Callback callback);
}
