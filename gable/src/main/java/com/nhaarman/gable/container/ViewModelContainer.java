package com.nhaarman.gable.container;

import java.util.Observable;
import org.jetbrains.annotations.NotNull;

public interface ViewModelContainer<O extends Observable> {

  void setViewModel(@NotNull O viewModel);

}
