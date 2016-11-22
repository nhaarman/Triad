package com.nhaarman.triad;

import android.support.annotation.NonNull;
import com.nhaarman.triad.Triad.Listener;

public final class TriadFactory {

    private TriadFactory() {
    }

    @NonNull
    public static Triad emptyInstance() {
        return TriadImpl.emptyInstance();
    }

    @NonNull
    public static Triad newInstance(@NonNull final Backstack backstack, @NonNull final Listener<?> listener) {
        return TriadImpl.newInstance(backstack, listener);
    }
}
