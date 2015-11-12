package com.nhaarman.triad;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public interface TransitionAnimator {

  boolean forward(@Nullable View oldView,
                  @NonNull View newView,
                  @NonNull Triad.Callback callback);

  boolean backward(@Nullable View oldView,
                  @NonNull View newView,
                  @NonNull Triad.Callback callback);
}
