package com.nhaarman.triad

import android.content.Intent

/**
 * Enriches this [Intent] with the necessary information to start given [Screen]s.
 * Whenever a new [android.app.Activity] is started with this [Intent], the
 * Triad backstack is populated with these [Screen]s.
 *
 * To be able to instantiate a [Screen], it must have one of the following:
 *
 *  - Either a parameterless constructor;
 *  - A static function [create(Intent)] in its companion object;
 *  - Or a top level function [create(Intent)] in the same file.
 */
fun Intent.startWith(vararg screens: Class<out Screen<*>>) {
    if (screens.size == 1) {
        putExtra("com.nhaarman.triad.screen", screens[0].canonicalName)
    } else {
        putExtra("com.nhaarman.triad.backstack", screens.map { it.canonicalName }.toTypedArray())
    }
}

/**
 * Creates a function that is able to create a single [Screen],
 * as set up by [startWith]. Returns [null] if no screen data
 * can be found.
 */
internal fun Intent.createScreen(): (() -> Screen<*>)? =
      getStringExtra("com.nhaarman.triad.screen")?.createMethod(this)

private fun Class<*>.parameterlessConstructor(): (() -> Screen<*>)? {
    return try {
        getConstructor()?.let {
            { it.newInstance() as Screen<*> }
        }
    } catch(e: NoSuchMethodException) {
        null
    }
}

private fun String.createMethod(intent: Intent): () -> Screen<*> =
      createClass()?.parameterlessConstructor() ?:
            createClass()?.createMethod(intent) ?:
            "${this}Kt".createClass()?.createMethod(intent) ?:
            throw error("Could not find empty constructor or `create(Intent)` method for class $this.")

private fun String.createClass(): Class<*>? {
    return try {
        Class.forName(this)
    } catch (e: ClassNotFoundException) {
        null
    }
}

internal fun Intent.createBackstack(): (() -> Backstack)? {
    return getStringArrayExtra("com.nhaarman.triad.backstack")?.let {
        val creates = it.map { it.createMethod(this) }
        return {
            Backstack.of(*creates.map { it() }.toTypedArray())
        }
    }
}

private fun Class<*>.createMethod(intent: Intent): (() -> Screen<*>)? {
    return methods.filter { it.name == "create" }.firstOrNull().let {
        if (it != null) {
            { (if (it.parameterTypes.size == 1) it(null, intent) else it(null)) as Screen <*> }
        } else {
            null
        }
    }
}

