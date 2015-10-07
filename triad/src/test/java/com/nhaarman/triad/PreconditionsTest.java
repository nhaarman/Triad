package com.nhaarman.triad;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PreconditionsTest {

  @Test(expected = NullPointerException.class)
  public void checkNotNull_withNullReference_throwsNullPointerException() {
    Preconditions.checkNotNull(null, "");
  }

  @Test
  public void checkNotNull_withNonNullReference_returnsThatReference() {
    /* Given */
    String reference = "Test";

    /* When */
    String result = Preconditions.checkNotNull(reference, "");

    /* Then */
    assertThat(result, is(reference));
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkArgument_withFalseArgument_throwsIllegalArgumentException() {
    Preconditions.checkArgument(false, "");
  }

  @Test
  public void checkArgument_withTrueArgument_doesNotThrow() {
    Preconditions.checkArgument(true, "");

    assertThat(i(), is(happy()));
  }

  @Test(expected = IllegalStateException.class)
  public void checkState_withFalseState_throwsIllegalStateException() {
    Preconditions.checkState(false, "");
  }

  @Test
  public void checkState_withTrueState_doesNotThrow() {
    Preconditions.checkState(true, "");

    assertThat(i(), is(happy()));
  }

  private Matcher<? super String> happy() {
    return new HappyMatcher();
  }

  private static String i() {
    return "I";
  }

  private static class HappyMatcher extends BaseMatcher<String> {

    @Override
    public boolean matches(final Object item) {
      return true;
    }

    @Override
    public void describeTo(final Description description) {

    }
  }
}