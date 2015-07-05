package com.nhaarman.triad;

import org.jetbrains.annotations.NotNull;

public interface TriadProvider {

  @NotNull
  Triad getTriad();
}
