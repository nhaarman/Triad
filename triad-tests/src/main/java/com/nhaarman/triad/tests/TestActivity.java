package com.nhaarman.triad.tests;

import com.nhaarman.triad.TriadActivity;
import com.nhaarman.triad.screen.Screen;
import com.nhaarman.triad.tests.firstscreen.FirstScreen;
import flow.Flow;
import org.jetbrains.annotations.NotNull;

public class TestActivity extends TriadActivity<TestComponent> {

  @NotNull
  @Override
  protected TestComponent createMainComponent() {
    return new TestComponent();
  }

  @NotNull
  @Override
  protected Screen<?, ?, TestComponent> createInitialScreen() {
    return new FirstScreen();
  }

  @NotNull
  @Override
  public Flow getFlow() {
    return super.getFlow();
  }
}
