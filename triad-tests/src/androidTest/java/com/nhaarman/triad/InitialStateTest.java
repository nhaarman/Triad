/*
 * Copyright 2015 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.triad;

import static org.assertj.android.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class InitialStateTest extends TestActivityInstrumentationTestCase {

  public void test_initially_screenHolderVisible() {
    assertThat(mScreenHolder).isVisible();
  }

  public void test_initially_screenHolderContainsFirstScreen() {
    assertThat(mScreenHolder.findViewById(com.nhaarman.triad.tests.R.id.view_screen_first), is(not(nullValue())));
  }
}
