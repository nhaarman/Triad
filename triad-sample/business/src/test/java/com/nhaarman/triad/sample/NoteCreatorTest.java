package com.nhaarman.triad.sample;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NoteCreatorTest {

  private NoteCreator mNoteCreator;

  private NoteRepository mNoteRepository;

  @Before
  public void setUp() {
    mNoteRepository = spy(new MemoryNoteRepository());
    mNoteCreator = new NoteCreator(mNoteRepository);
  }

  @Test
  public void creatingANote_isSuccessful() {
    /* Given */
    String title = "title";
    String contents = "contents";

    /* When */
    Note note = mNoteCreator.createNote(title, contents);

    /* Then */
    assertThat(note.getTitle(), is(title));
    assertThat(note.getContents(), is(contents));
    assertThat(note.getCreated(), is(greaterThan(System.currentTimeMillis() - 5000)));
  }

  @Test
  public void aCreatedNote_isPersisted() {
    /* Given */
    String title = "title";
    String contents = "contents";

    /* When */
    Note note = mNoteCreator.createNote(title, contents);

    /* Then */
    verify(mNoteRepository).create(note);
  }
}