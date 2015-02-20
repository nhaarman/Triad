package com.nhaarman.triad;

import android.view.View;
import android.view.ViewGroup;
import com.nhaarman.triad.container.ScreenContainer;
import com.nhaarman.triad.presenter.Presenter;
import com.nhaarman.triad.presenter.ScreenPresenter;
import com.nhaarman.triad.screen.Screen;
import flow.Flow;
import java.util.Stack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@link Presenter} which handles the showing of the {@link Screen}s in the application.
 * For each {@link Screen} that is to be shown, it sets up the {@link ScreenContainer} and {@link ScreenPresenter}
 * for that {@link Screen}, and performs the proper {@link View} adding and removal.
 *
 * @param <M> The {@code main component} to use for {@link Presenter} creation. See {@link TriadActivity}.
 */
class TriadPresenter<M> extends Presenter<TriadPresenter<M>, TriadContainer<M>> {

  @NotNull
  private final M mMainComponent;

  @NotNull
  private final Flow mFlow;

  @SuppressWarnings("rawtypes")
  @NotNull
  private final Stack<Screen> mDialogs;

  @NotNull
  private final Stack<ScreenContainer> mDialogContainers;

  @SuppressWarnings("rawtypes")
  @Nullable
  private Screen<? extends ScreenPresenter, ? extends ScreenContainer, M> mCurrentScreen;

  @Nullable
  private View mCurrentView;

  /**
   * Creates a new {@code TriadPresenter}.
   *
   * @param mainComponent The main component to use.
   * @param flow The {@link Flow} instance to use.
   */
  TriadPresenter(@NotNull final M mainComponent, @NotNull final Flow flow) {
    mMainComponent = mainComponent;
    mFlow = flow;

    mDialogs = new Stack<>();
    mDialogContainers = new Stack<>();
  }

  @Override
  protected void onControlGained(@NotNull final TriadContainer<M> container) {
    if (mCurrentScreen != null) {
      showScreen(mCurrentScreen, Flow.Direction.FORWARD);
    }
  }

  /**
   * Performs the proper transitions to show given {@link Screen}.
   *
   * @param screen The {@link Screen} to show.
   */
  public <P extends ScreenPresenter<P, C>, C extends ScreenContainer<P, C>> void showScreen(@NotNull final Screen<P, C, M> screen, final Flow.Direction direction) {
    mCurrentScreen = screen;

    if (getContainer() == null) {
      return;
    }

    if (direction == Flow.Direction.BACKWARD && !mDialogs.empty()) {
      popDialog();
    } else {
      showScreen(screen);
    }
  }

  private <P extends ScreenPresenter<P, C>, C extends ScreenContainer<P, C>> void showScreen(final Screen<P, C, M> screen) {
    if (getContainer() == null) {
      return;
    }

    C container = screen.createView((ViewGroup) getContainer());
    P presenter = screen.getPresenter(mMainComponent);
    container.setPresenter(presenter);

    boolean isDialog = screen.isDialog();
    if (isDialog) {
      getContainer().showDialog((View) container);
      mDialogs.push(screen);
      mDialogContainers.push(container);
    } else {
      getContainer().transition(mCurrentView, (View) container);
      mCurrentView = (View) container;
    }
  }

  private void popDialog() {
    if (getContainer() == null) {
      return;
    }

    mDialogs.pop();
    ScreenContainer container = mDialogContainers.pop();
    getContainer().dismissDialog((View) container);
  }

  /**
   * Called when the back button has been pressed.
   *
   * @return true if the event has been handled, false otherwise.
   */
  public boolean onBackPressed() {
    if (mDialogs.empty()) {
      return mCurrentScreen != null && mCurrentScreen.getPresenter(mMainComponent).onBackPressed() || mFlow.goBack();
    } else {
      Screen topDialog = mDialogs.peek();
      return topDialog.getPresenter(mMainComponent).onBackPressed() || mFlow.goBack();
    }
  }

  public void onDimmerClicked() {
    onBackPressed();
  }
}

