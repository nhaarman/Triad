package com.nhaarman.triad;

import android.app.Activity;
import com.nhaarman.triad.container.ScreenContainer;
import com.nhaarman.triad.presenter.ScreenPresenter;
import com.nhaarman.triad.screen.Screen;
import flow.Backstack;
import flow.Flow;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TriadManager<M> {

  /**
   * The {@link Activity} instance this {@code TriadManager} is bound to.
   */
  @NotNull
  private final Activity mActivity;

  /**
   * The {@link Flow} instance that is used to navigate between {@link Screen}s.
   */
  @Nullable
  private Flow mFlow;

  /**
   * The {@link TriadPresenter} that handles {@link Screen} navigation.
   */
  @Nullable
  private TriadPresenter<M> mTriadPresenter;

  @Nullable
  private OnScreenChangedListener<M> mOnScreenChangedListener;

  TriadManager(@NotNull final Activity activity) {
    mActivity = activity;
  }

  void onCreate(@NotNull final Screen<?, ?, M> initialScreen,
                @NotNull final M mainComponent) {
    initializeFlow(initialScreen);
    initializePresenter(mainComponent);
    initializeView();
  }

  /**
   * Retrieves the {@link Flow} instance, and registers this {@code TriadActivity} to be notified of screen transitions.
   */
  private void initializeFlow(@NotNull final Screen<?, ?, M> initialScreen) {
    FlowManager flowManager = FlowManager.getInstance(Backstack.single(initialScreen));
    flowManager.setFlowListener(new MyFlowListener());
    mFlow = flowManager.getFlow();
  }

  /**
   * Creates the {@link TriadPresenter}.
   */
  private void initializePresenter(@NotNull final M mainComponent) {
    assert mFlow != null;

    mTriadPresenter = new TriadPresenter(mainComponent, mFlow);
  }

  /**
   * Initializes the {@link TriadView}.
   */
  private void initializeView() {
    assert mTriadPresenter != null;

    mActivity.setContentView(R.layout.view_triad);
    TriadView<M> triadView = (TriadView<M>) mActivity.findViewById(R.id.view_triad);
    triadView.setPresenter(mTriadPresenter);
    mActivity.setContentView(triadView);
  }

  void onStart() {
    if (mTriadPresenter == null) {
      throw new IllegalStateException("TriadPresenter is null. Make sure to call TriadManager.onCreate(Screen<?,?,M>, M).");
    }

    mTriadPresenter.onStart();
  }

  void onPostCreate() {
    if (mTriadPresenter == null) {
      throw new IllegalStateException("TriadPresenter is null. Make sure to call TriadManager.onCreate(Screen<?,?,M>, M).");
    }
    if (mFlow == null) {
      throw new IllegalStateException("Flow is null. Make sure to call TriadManager.onCreate(Screen<?,?,M>, M).");
    }

    Backstack backstack = mFlow.getBackstack();
    AbstractList<Screen<?, ?, M>> screens = new ArrayList<>();

    for (Iterator<Backstack.Entry> iterator1 = backstack.reverseIterator(); iterator1.hasNext(); ) {
      Screen<?, ?, M> screen = (Screen<?, ?, M>) iterator1.next().getScreen();
      if (!screen.isDialog()) {
        screens.clear();
      }

      screens.add(screen);
    }

    Screen lastScreen = null;
    for (Screen screen : screens) {
      mTriadPresenter.showScreen(screen, Flow.Direction.FORWARD);
      lastScreen = null;
    }
    if (lastScreen != null) {
      onScreenChanged(lastScreen);
    }
  }

  void onStop() {
    if (mTriadPresenter == null) {
      throw new IllegalStateException("TriadPresenter is null. Make sure to call TriadManager.onCreate(Screen<?,?,M>, M).");
    }
    mTriadPresenter.onStop();
  }

  boolean onBackPressed() {
    if (mTriadPresenter == null) {
      throw new IllegalStateException("TriadPresenter is null. Make sure to call TriadManager.onCreate(Screen<?,?,M>, M).");
    }
    return mTriadPresenter.onBackPressed();
  }

  /**
   * Returns the {@link Flow} instance to be used to navigate between {@link Screen}s.
   */
  @NotNull
  Flow getFlow() {
    if (mFlow == null) {
      throw new IllegalStateException("Flow is null. Make sure to call TriadManager.onCreate(Screen<?,?,M>, M).");
    }
    return mFlow;
  }

  public void setOnScreenChangedListener(@Nullable final OnScreenChangedListener<M> onScreenChangedListener) {
    mOnScreenChangedListener = onScreenChangedListener;
  }

  private void onScreenChanged(@NotNull final Screen<?, ?, M> screen) {
    if (mOnScreenChangedListener != null) {
      mOnScreenChangedListener.onScreenChanged(screen);
    }
  }

  /**
   * A {@link Flow.Listener} that delegates {@link Screen} transitions to the {@link TriadPresenter}.
   */
  private class MyFlowListener implements Flow.Listener {

    @Override
    public void go(final Backstack nextBackstack, final Flow.Direction direction, final Flow.Callback callback) {
      if (mTriadPresenter == null) {
        throw new IllegalStateException("TriadPresenter is null. Make sure to call TriadManager.onCreate(Screen<?,?,M>, M).");
      }

      //noinspection rawtypes
      Screen<? extends ScreenPresenter, ? extends ScreenContainer, M> screen =
          (Screen<? extends ScreenPresenter, ? extends ScreenContainer, M>) nextBackstack.current().getScreen();
      mTriadPresenter.showScreen(screen, direction);
      callback.onComplete();

      onScreenChanged(screen);
    }
  }
}
