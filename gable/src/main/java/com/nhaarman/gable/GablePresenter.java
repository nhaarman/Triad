package com.nhaarman.gable;

import android.view.View;
import android.view.ViewGroup;
import com.nhaarman.gable.container.ScreenContainer;
import com.nhaarman.gable.presenter.Presenter;
import com.nhaarman.gable.presenter.ScreenPresenter;
import com.nhaarman.gable.screen.Screen;
import flow.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@link Presenter} which handles the showing of the {@link Screen}s in the application.
 * For each {@code Screen} that is to be shown, it sets up the {@link ScreenContainer} and {@link ScreenPresenter}
 * for that {@code Screen}, and performs the proper {@link View} adding and removal.
 *
 * Since the creation of a {@link Presenter} may need additional dependencies, a {@code main component} is supplied
 * when requesting the {@code Presenter} instance. This {@code main component} should contain all dependencies necessary
 * for the {@code Presenter}, and is to be supplied by the implementer.
 *
 * @param <M> The {@code main component} to use for {@code Presenter} creation.
 */
class GablePresenter<M> extends Presenter<GablePresenter<M>, GableContainer<M>> {

  @NotNull
  private final M mMainComponent;

  @NotNull
  private final Flow mFlow;

  @SuppressWarnings("rawtypes")
  @Nullable
  private Screen<? extends ScreenPresenter, ? extends ScreenContainer, M> mCurrentScreen;

  @Nullable
  private View mCurrentView;

  /**
   * Creates a new {@code GablePresenter}.
   *
   * @param mainComponent The main component to use.
   * @param flow The {@link Flow} instance to use.
   */
  GablePresenter(@NotNull final M mainComponent, @NotNull final Flow flow) {
    mMainComponent = mainComponent;
    mFlow = flow;
  }

  @Override
  protected void onControlGained(@NotNull final GableContainer<M> container) {
    if (mCurrentScreen != null) {
      showScreen(mCurrentScreen);
    }
  }

  /**
   * Performs the proper transitions to show given {@link Screen}.
   *
   * @param screen The {@code Screen} to show.
   * @param <P> The {@link ScreenPresenter} type.
   * @param <C> The {@link ScreenContainer} type.
   */
  public <P extends ScreenPresenter<P, C>, C extends ScreenContainer<P, C>> void showScreen(@NotNull final Screen<P, C, M> screen) {
    mCurrentScreen = screen;

    if (getContainer() == null) {
      return;
    }

    C container = screen.createView((ViewGroup) getContainer());
    P presenter = screen.getPresenter(mMainComponent);
    container.setPresenter(presenter);

    getContainer().transition(mCurrentView, (View) container);

    mCurrentView = (View) container;
  }

  /**
   * Called when the back button has been pressed.
   *
   * @return true if the event has been handled, false otherwise.
   */
  public boolean onBackPressed() {
    return mCurrentScreen != null && mCurrentScreen.getPresenter(mMainComponent).onBackPressed() || mFlow.goBack();
  }
}

