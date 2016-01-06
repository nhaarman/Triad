package com.nhaarman.triad;

import android.app.Activity;
import android.app.Application;
import android.view.View;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockSettings;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.mockito.MockitoAnnotations.initMocks;

public class TriadDelegateTest {

    @Mock
    private Activity mActivity;

    private Application mApplication;

    @Mock
    private Triad.Listener<Object> mListener;

    @Mock
    private Screen<Object> mScreen1;

    @Mock
    private Screen<Object> mScreen2;

    @Before
    public void setup() {
        initMocks(this);

        mApplication = mock(Application.class, withSettings().extraInterfaces(TriadProvider.class, ApplicationComponentProvider.class));

        when(mActivity.getApplication()).thenReturn(mApplication);
        when(mActivity.findViewById(anyInt())).thenReturn(mock(TriadView.class));
    }

    @Test
    public void onDestroy_notifiesBackstackScreenPopped() {
        /* Given */
        TriadDelegate<Object> delegate = TriadDelegate.createFor(mActivity);
        when(((TriadProvider) mApplication).getTriad()).thenReturn(Triad.newInstance(Backstack.of(mScreen1, mScreen2), mListener));
        delegate.onCreate();

        /* When */
        delegate.onDestroy();

        /* Then */
        verify(mScreen2).onDestroy();
        verify(mScreen1).onDestroy();
    }
}