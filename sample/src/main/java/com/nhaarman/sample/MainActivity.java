package com.nhaarman.sample;

import com.nhaarman.gable.GableActivity;
import com.nhaarman.gable.screen.Screen;
import com.nhaarman.sample.main.MainScreen;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends GableActivity<MainComponent> {

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
