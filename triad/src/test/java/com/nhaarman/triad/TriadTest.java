/*
 * Copyright 2015 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.triad;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class TriadTest {

    @Mock
    private Triad.Listener<Object> mListener;

    @Mock
    private Screen<Object> mScreen1;

    @Mock
    private Screen<Object> mScreen2;

    @Mock
    private Screen<Object> mScreen3;

    @Before
    public void setUp() {
        initMocks(this);

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocationOnMock) throws Throwable {
                Triad.Callback callback = (Triad.Callback) invocationOnMock.getArguments()[2];
                callback.onComplete();
                return null;
            }
        }).when(mListener).forward(any(Screen.class), any(TransitionAnimator.class), any(Triad.Callback.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocationOnMock) throws Throwable {
                Triad.Callback callback = (Triad.Callback) invocationOnMock.getArguments()[2];
                callback.onComplete();
                return null;
            }
        }).when(mListener).backward(any(Screen.class), any(TransitionAnimator.class), any(Triad.Callback.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocationOnMock) throws Throwable {
                Triad.Callback callback = (Triad.Callback) invocationOnMock.getArguments()[2];
                callback.onComplete();
                return null;
            }
        }).when(mListener).replace(any(Screen.class), any(TransitionAnimator.class), any(Triad.Callback.class));
    }

    @Test(expected = IllegalStateException.class)
    public void startWith_withoutListener_throwsIllegalStateException() {
    /* Given */
        Triad triad = Triad.emptyInstance();

    /* When */
        triad.startWith(mock(Screen.class));
    }

    @Test
    public void startWith_firstTime_forwardsToScreen() {
    /* Given */
        Triad triad = Triad.emptyInstance();
        triad.setListener(mListener);

    /* When */
        triad.startWith(mScreen1);

    /* Then */
        verify(mListener).forward(eq(mScreen1), any(TransitionAnimator.class), any(Triad.Callback.class));
        assertBackstackHasEntries(triad.getBackstack(), mScreen1);
    }

    @Test
    public void startWith_secondTime_doesNotMoveForward() {
    /* Given */
        Triad triad = Triad.emptyInstance();
        triad.setListener(mListener);
        triad.startWith(mScreen1);
        verify(mListener).forward(eq(mScreen1), any(TransitionAnimator.class), any(Triad.Callback.class));

    /* When */
        triad.startWith(mScreen1);

    /* Then */
        verifyNoMoreInteractions(mListener);
        assertBackstackHasEntries(triad.getBackstack(), mScreen1);
    }

    @Test
    public void startWith_withAValidBackstack_doesNotMoveForward() {
    /* Given */
        Triad triad = Triad.newInstance(Backstack.single(mScreen1), mListener);

    /* When */
        triad.startWith(mScreen1);

    /* Then */
        verifyNoMoreInteractions(mListener);
        assertBackstackHasEntries(triad.getBackstack(), mScreen1);
    }

    @Test(expected = IllegalStateException.class)
    public void goTo_withoutHavingABackstack_throwsIllegalStateException() {
    /* Given */
        Triad triad = Triad.emptyInstance();
        triad.setListener(mListener);

    /* When */
        triad.goTo(mScreen1);
    }

    @Test
    public void goTo_withAValidBackstack_forwardsToScreen() {
    /* Given */
        Triad triad = Triad.emptyInstance();
        triad.setListener(mListener);
        triad.startWith(mScreen1);
        verify(mListener).forward(eq(mScreen1), any(TransitionAnimator.class), any(Triad.Callback.class));

    /* When */
        triad.goTo(mScreen2);

    /* Then */
        verify(mListener).forward(eq(mScreen2), any(TransitionAnimator.class), any(Triad.Callback.class));
        assertBackstackHasEntries(triad.getBackstack(), mScreen1, mScreen2);
    }

    @Test(expected = IllegalStateException.class)
    public void popTo_withAnEmptyBackstack_throwsIllegalStateException() {
    /* Given */
        Triad triad = Triad.emptyInstance();
        triad.setListener(mListener);

    /* When */
        triad.popTo(mScreen1);
    }

    @Test
    public void popTo_withScreenNotOnBackstack_forwardsToScreen() {
    /* Given */
        Triad triad = Triad.emptyInstance();
        triad.setListener(mListener);
        triad.startWith(mScreen1);

    /* When */
        triad.popTo(mScreen2);

    /* Then */
        verify(mListener).forward(eq(mScreen2), any(TransitionAnimator.class), any(Triad.Callback.class));
        assertBackstackHasEntries(triad.getBackstack(), mScreen1, mScreen2);
    }

    @Test
    public void popTo_withScreenOnBackstack_backwardsToScreen() {
    /* Given */
        Triad triad = Triad.newInstance(Backstack.of(mScreen1, mScreen2), mListener);

    /* When */
        triad.popTo(mScreen1);

    /* Then */
        verify(mListener).backward(eq(mScreen1), any(TransitionAnimator.class), any(Triad.Callback.class));
        assertBackstackHasEntries(triad.getBackstack(), mScreen1);
    }

    @Test
    public void popTo_withScreenOnTop_doesNothing() {
    /* Given */
        Triad triad = Triad.newInstance(Backstack.single(mScreen1), mListener);

    /* When */
        triad.popTo(mScreen1);

    /* Then */
        verifyNoMoreInteractions(mListener);
        assertBackstackHasEntries(triad.getBackstack(), mScreen1);
    }

    @Test(expected = IllegalStateException.class)
    public void replaceWith_withAnEmptyBackstack_throwsIllegalStateException() {
    /* Given */
        Triad triad = Triad.emptyInstance();
        triad.setListener(mListener);

    /* When */
        triad.replaceWith(mScreen1);
    }

    @Test
    public void replaceWith_withAValidBackstack_replacesScreen() {
    /* Given */
        Triad triad = Triad.newInstance(Backstack.single(mScreen1), mListener);

    /* When */
        triad.replaceWith(mScreen2);

    /* Then */
        verify(mListener).replace(eq(mScreen2), any(TransitionAnimator.class), any(Triad.Callback.class));
        assertBackstackHasEntries(triad.getBackstack(), mScreen2);
    }

    @Test(expected = IllegalStateException.class)
    public void goBack_withAnEmptyBackstack_throwsIllegalStateException() {
    /* Given */
        Triad triad = Triad.emptyInstance();

    /* When */
        triad.goBack();
    }

    @Test
    public void goBack_withSingleBackstackEntry_doesNothing() {
    /* Given */
        Triad triad = Triad.newInstance(Backstack.single(mScreen1), mListener);

    /* When */
        boolean result = triad.goBack();

    /* Then */
        assertThat(result, is(false));
        assertBackstackHasEntries(triad.getBackstack(), mScreen1);
    }

    @Test
    public void goBack_withMoreThanOneBackstackEntry_backwards() {
    /* Given */
        Triad triad = Triad.newInstance(Backstack.of(mScreen1, mScreen2), mListener);

    /* When */
        boolean result = triad.goBack();

    /* Then */
        assertThat(result, is(true));
        assertBackstackHasEntries(triad.getBackstack(), mScreen1);
    }

    @Test(expected = IllegalStateException.class)
    public void forward_withAnEmptyBackstack_throwsIllegalStateException() {
    /* Given */
        Triad triad = Triad.emptyInstance();
        triad.setListener(mListener);

    /* When */
        triad.forward(Backstack.of(mScreen1, mScreen2));
    }

    @Test
    public void forward_withAValidBackstack_forwardsToLastScreen() {
    /* Given */
        Triad triad = Triad.newInstance(Backstack.single(mScreen1), mListener);

    /* When */
        triad.forward(Backstack.of(mScreen2, mScreen3));

    /* Then */
        verify(mListener).forward(eq(mScreen3), any(TransitionAnimator.class), any(Triad.Callback.class));
        assertBackstackHasEntries(triad.getBackstack(), mScreen2, mScreen3);
    }

    @Test(expected = IllegalStateException.class)
    public void backward_withAnEmptyBackstack_throwsIllegalStateException() {
    /* Given */
        Triad triad = Triad.emptyInstance();
        triad.setListener(mListener);

    /* When */
        triad.backward(Backstack.of(mScreen1, mScreen2));
    }

    @Test
    public void backward_withAValidBackstack_backwardsToLastScreen() {
    /* Given */
        Triad triad = Triad.newInstance(Backstack.single(mScreen1), mListener);

    /* When */
        triad.backward(Backstack.of(mScreen2, mScreen3));

    /* Then */
        verify(mListener).backward(eq(mScreen3), any(TransitionAnimator.class), any(Triad.Callback.class));
        assertBackstackHasEntries(triad.getBackstack(), mScreen2, mScreen3);
    }

    @Test(expected = IllegalStateException.class)
    public void replace_withAnEmptyBackstack_throwsIllegalStateException() {
    /* Given */
        Triad triad = Triad.emptyInstance();
        triad.setListener(mListener);

    /* When */
        triad.replace(Backstack.of(mScreen1, mScreen2));
    }

    @Test
    public void replace_withAValidBackstack_replacesToLastScreen() {
    /* Given */
        Triad triad = Triad.newInstance(Backstack.single(mScreen1), mListener);

    /* When */
        triad.replace(Backstack.of(mScreen2, mScreen3));

    /* Then */
        verify(mListener).replace(eq(mScreen3), any(TransitionAnimator.class), any(Triad.Callback.class));
        assertBackstackHasEntries(triad.getBackstack(), mScreen2, mScreen3);
    }

    @Test(expected = IllegalStateException.class)
    public void startActivity_withoutActivityReference_throwsIllegalStateException() {
    /* Given */
        Triad triad = Triad.newInstance(Backstack.single(mScreen1), mListener);

    /* When */
        triad.startActivity(mock(Intent.class));
    }

    @Test
    public void startActivity_withActivityReference_startsActivity() {
    /* Given */
        Activity activity = mock(Activity.class);
        Intent intent = mock(Intent.class);

        Triad triad = Triad.newInstance(Backstack.single(mScreen1), mListener);
        triad.setActivity(activity);

    /* When */
        triad.startActivity(intent);

    /* Then */
        verify(activity).startActivity(intent);
    }

    @Test(expected = IllegalStateException.class)
    public void startActivityForResult_withoutActivityReference_throwsIllegalStateException() {
    /* Given */
        Triad triad = Triad.newInstance(Backstack.single(mScreen1), mListener);

    /* When */
        triad.startActivityForResult(mock(Intent.class), mock(Triad.ActivityResultListener.class));
    }

    @Test
    public void startActivityForResult_withActivityReference_startsActivityForResult() {
    /* Given */
        Activity activity = mock(Activity.class);
        Intent intent = mock(Intent.class);
        Triad.ActivityResultListener resultListener = mock(Triad.ActivityResultListener.class);

        Triad triad = Triad.newInstance(Backstack.single(mScreen1), mListener);
        triad.setActivity(activity);

    /* When */
        triad.startActivityForResult(intent, resultListener);

    /* Then */
        verify(activity).startActivityForResult(eq(intent), anyInt());
    }

    @SuppressWarnings("rawtypes")
    private static void assertBackstackHasEntries(@NonNull final Backstack backstack, @NonNull final Screen... screens) {
        assertThat("Backstack size", backstack.size(), is(screens.length));

        int i = 0;
        for (Iterator<Screen<?>> iterator = backstack.reverseIterator(); iterator.hasNext(); ) {
            Screen screen = iterator.next();
            assertThat(backstack.toString(), screen, is(screens[i]));
            i++;
        }
    }
}