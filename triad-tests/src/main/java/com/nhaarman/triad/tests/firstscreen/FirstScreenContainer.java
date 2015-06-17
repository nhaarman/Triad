package com.nhaarman.triad.tests.firstscreen;

import com.nhaarman.triad.ScreenContainer;

public interface FirstScreenContainer extends ScreenContainer<FirstScreenPresenter, FirstScreenContainer> {

  void setText(String text);
}
