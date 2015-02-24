package com.nhaarman.triad.sample.editnote;

import com.nhaarman.triad.TraidActivityInstrumentationTestCase;
import com.nhaarman.triad.sample.MainActivity;
import com.nhaarman.triad.sample.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class EditNewNoteScreenTest extends TraidActivityInstrumentationTestCase<MainActivity> {

  private static final String TITLE = "Some title";
  private static final String CONTENTS = "Some contents";

  public EditNewNoteScreenTest() {
    super(MainActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    getActivity();
    onView(withText(R.string.create_note)).perform(click());
    getInstrumentation().waitForIdleSync();
  }

  public void test_editNoteScreen_hasTitleEditText() {
    onView(withHint(R.string.title)).check(matches(isDisplayed()));
  }

  public void test_editNoteScreen_hasContentsEditText() {
    onView(withHint(R.string.contents)).check(matches(isDisplayed()));
  }

  public void test_editNoteScreen_hasSaveButton() {
    onView(withText(R.string.save)).check(matches(isDisplayed()));
  }

  public void test_savingEmptyTitle_showsError() {
    /* When */
    onView(withText(R.string.save)).perform(click());

    /* Then */
    onView(withHint(R.string.error_title)).check(matches(isDisplayed()));
  }

  public void test_savingEmptyContents_showsError() {
    /* Given */
    onView(withHint(R.string.title)).perform(typeText(TITLE));

    /* When */
    onView(withText(R.string.save)).perform(click());

    /* Then */
    onView(withHint(R.string.error_contents)).check(matches(isDisplayed()));
  }

  public void test_savingValidContents_movesBackToNotesScreen() {
    /* Given */
    onView(withHint(R.string.title)).perform(typeText(TITLE));
    onView(withHint(R.string.contents)).perform(typeText(CONTENTS));

    /* When */
    onView(withText(R.string.save)).perform(click());

    /* Then */
    onView(withId(R.id.view_notes)).check(matches(isDisplayed()));
  }
}
