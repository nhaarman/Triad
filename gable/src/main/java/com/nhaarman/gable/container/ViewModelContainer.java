package com.nhaarman.gable.container;

import java.util.Observable;
import org.jetbrains.annotations.NotNull;

public interface ViewModelContainer<M extends Observable> {

  void setViewModel(@NotNull M viewModel);

}
