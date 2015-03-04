package com.nhaarman.triad.sample.editnote;

import com.nhaarman.triad.TriadActivityInstrumentationTestCase;
import com.nhaarman.triad.sample.MainActivity;
import com.nhaarman.triad.sample.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class EditExistingNoteScreenTest extends TriadActivityInstrumentationTestCase<MainActivity> {

  private static final String TITLE = "Some title";
  private static final String CONTENTS = "Some contents";

  public EditExistingNoteScreenTest() {
    super(MainActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    getActivity();
    createNote(TITLE, CONTENTS);
    onView(withText(TITLE)).perform(click());
    getInstrumentation().waitForIdleSync();
    Thread.sleep(500);
  }

  public void test_editNoteScreen_hasTitleEditText() throws InterruptedException {
    onView(withHint(R.string.title)).check(matches(withText(TITLE)));
  }

  public void test_editNoteScreen_hasContentsEditText() {
    onView(withHint(R.string.contents)).check(matches(withText(CONTENTS)));
  }

  public void test_editNoteScreen_hasSaveButton() {
    onView(withText(R.string.save)).check(matches(isDisplayed()));
  }

  public void test_savingEmptyTitle_showsError() {
    /* Given */
    onView(withText(TITLE)).perform(clearText());

    /* When */
    onView(withText(R.string.save)).perform(click());

    /* Then */
    onView(withHint(R.string.error_title)).check(matches(isDisplayed()));
  }

  public void test_savingEmptyContents_showsError() {
    /* Given */
    onView(withText(CONTENTS)).perform(clearText());

    /* When */
    onView(withText(R.string.save)).perform(click());

    /* Then */
    onView(withHint(R.string.error_contents)).check(matches(isDisplayed()));
  }

  public void test_savingValidContents_movesBackToNotesScreen() {
    /* When */
    onView(withText(R.string.save)).perform(click());

    /* Then */
    onView(withId(R.id.view_notes)).check(matches(isDisplayed()));
  }
}
