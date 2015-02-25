package com.nhaarman.triad.sample;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class NoteValidatorTest {

  private NoteValidator mNoteValidator;

  @Before
  public void setUp() {
    mNoteValidator = new NoteValidator();
  }

  @Test
  public void aValidTitle_validates() {
    /* Given */
    String title = "title";

    /* When */
    boolean isValid = mNoteValidator.validateTitle(title);

    /* Then */
    assertThat(isValid, is(true));
  }

  @Test
  public void aNullTitle_doesNotValidate() {
    /* Given */
    String title = null;

    /* When */
    boolean isValid = mNoteValidator.validateTitle(title);

    /* Then */
    assertThat(isValid, is(false));
  }

  @Test
  public void anEmptyTitle_doesNotValidate() {
    /* Given */
    String title = "";

    /* When */
    boolean isValid = mNoteValidator.validateTitle(title);

    /* Then */
    assertThat(isValid, is(false));
  }

  @Test
  public void aTitleWithOnlyWhiteSpace_doesNotValidate() {
    /* Given */
    String title = "  ";

    /* When */
    boolean isValid = mNoteValidator.validateTitle(title);

    /* Then */
    assertThat(isValid, is(false));
  }

  /* ---------------------------------------------------------------------- */

  @Test
  public void validContents_validates() {
    /* Given */
    String contents = "contents";

    /* When */
    boolean isValid = mNoteValidator.validateContents(contents);

    /* Then */
    assertThat(isValid, is(true));
  }

  @Test
  public void nullContents_doesNotValidate() {
    /* Given */
    String contents = null;

    /* When */
    boolean isValid = mNoteValidator.validateContents(contents);

    /* Then */
    assertThat(isValid, is(false));
  }

  @Test
  public void emptyContents_doesNotValidate() {
    /* Given */
    String contents = "";

    /* When */
    boolean isValid = mNoteValidator.validateContents(contents);

    /* Then */
    assertThat(isValid, is(false));
  }

  @Test
  public void contentsWithOnlyWhiteSpace_doesNotValidate() {
    /* Given */
    String contents = "  ";

    /* When */
    boolean isValid = mNoteValidator.validateContents(contents);

    /* Then */
    assertThat(isValid, is(false));
  }
}