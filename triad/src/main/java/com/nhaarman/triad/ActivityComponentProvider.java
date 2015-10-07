package com.nhaarman.triad;

import android.support.annotation.NonNull;

public interface ActivityComponentProvider<ActivityComponent> {

  @NonNull
  ActivityComponent getActivityComponent();
}
