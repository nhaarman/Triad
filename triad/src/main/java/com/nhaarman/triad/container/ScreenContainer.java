package com.nhaarman.triad.container;

import com.nhaarman.triad.presenter.ScreenPresenter;
import com.nhaarman.triad.screen.Screen;

/**
 * A {@link Container} interface that is used in combination with a {@link Screen} and a {@link ScreenPresenter}.
 *
 * @param <P> The specialized {@link ScreenPresenter} type.
 * @param <C> The specialized {@link ScreenContainer} type.
 */
public interface ScreenContainer<P extends ScreenPresenter<P, C>, C extends Container<P, C>> extends Container<P, C> {

}
