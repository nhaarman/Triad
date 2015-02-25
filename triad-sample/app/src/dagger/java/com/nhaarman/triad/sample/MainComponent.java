package com.nhaarman.triad.sample;

import dagger.Component;
import flow.Flow;

@MainScope
@Component(modules = MainModule.class)
public interface MainComponent {

  Flow flow();

  NoteRepository noteRepository();
}
