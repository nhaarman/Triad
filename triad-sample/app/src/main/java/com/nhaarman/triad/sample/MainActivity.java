package com.nhaarman.triad.sample;

import android.os.Bundle;
import com.nhaarman.triad.TriadActivity;
import com.nhaarman.triad.sample.notes.NotesScreen;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends TriadActivity<MainComponent> {

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getTriad().startWith(new NotesScreen());
  }

  @Override
  @NotNull
  protected MainComponent createActivityComponent() {
    return new MainComponent();
  }
}
