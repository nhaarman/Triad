package com.nhaarman.triad;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class TriadIntents {

    private static final String KEY_SCREEN = "com.nhaarman.triad.screen";
    private static final String KEY_BACKSTACK = "com.nhaarman.triad.backstack";

    private TriadIntents() {
    }

    public static void startWith(
          @NonNull final Intent intent,
          @NonNull final Class<? extends Screen> screen,
          @NonNull final Class<? extends Screen>... screens
    ) {
        if (screens.length == 0) {
            intent.putExtra(KEY_SCREEN, screen.getCanonicalName());
        } else {
            String[] classNames = new String[screens.length + 1];

            classNames[0] = screen.getCanonicalName();
            for (int i = 0; i < screens.length; i++) {
                classNames[i + 1] = screens[i].getCanonicalName();
            }

            intent.putExtra(KEY_BACKSTACK, classNames);
        }
    }

    @Nullable
    public static Screen<?> createScreen(@NonNull final Intent intent) {
        String screenClassName = intent.getStringExtra(KEY_SCREEN);
        if (screenClassName == null) return null;

        return createScreen(intent, screenClassName);
    }

    @NonNull
    private static Screen<?> createScreen(@NonNull final Intent intent, @NonNull final String screenClassName) {
        Class<?> screenClass = createClassForName(screenClassName);
        if (screenClass == null) {
            throw new IllegalStateException("Could not create a class for " + screenClassName);
        }

        Screen<?> screenInstance = createScreenInstanceUsingConstructor(screenClass);
        if (screenInstance != null) return screenInstance;

        screenInstance = createScreenInstanceUsingCreateMethod(screenClass, intent);
        if (screenInstance != null) return screenInstance;

        Class<?> kotlinScreenClass = createClassForName(screenClassName + "Kt");
        if (kotlinScreenClass == null) {
            throw new IllegalStateException("Could not find empty constructor, `create()` or `create(Intent)` method for class " + screenClassName);
        }

        screenInstance = createScreenInstanceUsingCreateMethod(kotlinScreenClass, intent);
        if (screenInstance != null) return screenInstance;

        throw new IllegalStateException("Could not find empty constructor, `create()` or `create(Intent)` method for class " + screenClassName);
    }

    @Nullable
    public static Backstack createBackstack(@NonNull final Intent intent) {
        String[] backstack = intent.getStringArrayExtra(KEY_BACKSTACK);
        if (backstack == null) return null;

        Screen<?>[] screens = new Screen[backstack.length];
        for (int i = 0; i < backstack.length; i++) {
            screens[i] = createScreen(intent, backstack[i]);
        }

        return Backstack.of(screens);
    }

    @Nullable
    private static Class<?> createClassForName(@NonNull final String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            Log.w("Triad", "Could not create class for name " + className, e);
            return null;
        }
    }

    @Nullable
    private static Screen<?> createScreenInstanceUsingConstructor(@NonNull final Class<?> screenClass) {
        Constructor<?> constructor = parameterlessConstructor(screenClass);
        if (constructor == null) return null;

        try {
            return (Screen<?>) screenClass.newInstance();
        } catch (InstantiationException e) {
            Log.w("Triad", "Could not create screen instance for " + screenClass.getCanonicalName(), e);
            return null;
        } catch (IllegalAccessException e) {
            Log.w("Triad", "Could not create screen instance for " + screenClass.getCanonicalName(), e);
            return null;
        }
    }

    @Nullable
    private static Constructor<?> parameterlessConstructor(@NonNull final Class<?> screenClass) {
        try {
            return screenClass.getConstructor();
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @Nullable
    private static Screen<?> createScreenInstanceUsingCreateMethod(@NonNull final Class<?> screenClass, @NonNull final Intent intent) {
        Method createMethod = null;
        for (final Method method : screenClass.getMethods()) {
            if (method.getName().equals("create")) {
                createMethod = method;
            }
        }

        if (createMethod == null) return null;

        try {
            if (createMethod.getParameterTypes().length == 1) {
                return (Screen<?>) createMethod.invoke(null, intent);
            } else {
                return (Screen<?>) createMethod.invoke(null);
            }
        } catch (InvocationTargetException e) {
            Log.w("Triad", "Could not create screen instance for " + screenClass.getCanonicalName(), e);
            return null;
        } catch (IllegalAccessException e) {
            Log.w("Triad", "Could not create screen instance for " + screenClass.getCanonicalName(), e);
            return null;
        }
    }
}
