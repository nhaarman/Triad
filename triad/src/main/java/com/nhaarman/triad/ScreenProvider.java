package com.nhaarman.triad;

import android.support.annotation.NonNull;

public interface ScreenProvider<ApplicationComponent> {

  @NonNull
  Screen<ApplicationComponent> getCurrentScreen();
}
