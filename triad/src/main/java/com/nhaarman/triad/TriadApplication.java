package com.nhaarman.triad;

import android.app.Application;
import android.os.Debug;
import org.jetbrains.annotations.NotNull;

public class TriadApplication extends Application implements TriadProvider {

  private Triad mTriad;

  @Override
  public void onCreate() {
    super.onCreate();
    mTriad = Triad.emptyInstance();
  }

  @Override
  @NotNull
  public Triad getTriad() {
    return mTriad;
  }
}
