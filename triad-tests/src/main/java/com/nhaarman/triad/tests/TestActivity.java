package com.nhaarman.triad.tests;

import android.os.Bundle;
import com.nhaarman.triad.Triad;
import com.nhaarman.triad.TriadActivity;
import com.nhaarman.triad.tests.firstscreen.FirstScreen;
import org.jetbrains.annotations.NotNull;

public class TestActivity extends TriadActivity<TestComponent> {

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getTriad().startWith(new FirstScreen());
  }

  @NotNull
  @Override
  protected TestComponent createActivityComponent() {
    return new TestComponent();
  }

  @NotNull
  @Override
  public Triad getTriad() {
    return super.getTriad();
  }
}
