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

import com.nhaarman.triad.tests.secondscreen.SecondScreen;

import static com.nhaarman.triad.utils.ViewWaiter.viewNotPresent;
import static com.nhaarman.triad.utils.ViewWaiter.waitUntil;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class BackwardScreenTransitionTest extends TestActivityInstrumentationTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    getInstrumentation().runOnMainSync(new Runnable() {
      @Override
      public void run() {
        mTriad.goTo(new SecondScreen());
      }
    });

    waitUntil(viewNotPresent(mScreenHolder, com.nhaarman.triad.tests.R.id.view_screen_first));

    getInstrumentation().runOnMainSync(new Runnable() {
      @Override
      public void run() {
        mTriad.goBack();
      }
    });

    waitUntil(viewNotPresent(mScreenHolder, com.nhaarman.triad.tests.R.id.view_screen_second));
  }

  public void test_afterTransition_viewsHaveBeenSwitched() throws InterruptedException {
    assertThat(mScreenHolder.findViewById(com.nhaarman.triad.tests.R.id.view_screen_first), is(not(nullValue())));
    assertThat(mScreenHolder.findViewById(com.nhaarman.triad.tests.R.id.view_screen_second), is(nullValue()));
  }
}
