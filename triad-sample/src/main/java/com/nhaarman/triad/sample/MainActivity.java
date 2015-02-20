package com.nhaarman.triad.sample;

import com.nhaarman.triad.TriadActivity;
import com.nhaarman.triad.screen.Screen;
import com.nhaarman.triad.sample.main.MainScreen;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends TriadActivity<MainComponent> {

  @Override
  @NotNull
  protected MainComponent createMainComponent() {
    return new MainComponent();
  }

  @NotNull
  @Override
  protected Screen<?, ?, MainComponent> createInitialScreen() {
    return new MainScreen();
  }
}
