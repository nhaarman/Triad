package com.nhaarman.triad;

import android.app.Activity;
import com.nhaarman.triad.container.ScreenContainer;
import com.nhaarman.triad.presenter.Presenter;
import com.nhaarman.triad.presenter.ScreenPresenter;
import com.nhaarman.triad.screen.Screen;
import flow.Backstack;
import flow.Flow;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a delegate which can be used to use Triad in any
 * {@link Activity}.
 * <p>
 * When using the {@code TriadDelegate}, you must proxy the following Activity
 * lifecycle methods to it:
 * <ul>
 * <li>{@link #onCreate(Screen, Object)}</li>
 * <li>{@link #onStart()}</li>
 * <li>{@link #onPostCreate()}</li>
 * <li>{@link #onStop()}</li>
 * <li>{@link #onBackPressed()}</li>
 * </ul>
 *
 * @param <M> The {@code main component} to use for {@link Presenter} creation.
 */
public class TriadDelegate<M> {

  /**
   * The {@link Activity} instance this {@code TriadDelegate} is bound to.
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

  /**
   * An optional {@link OnScreenChangedListener} that is notified of screen changes.
   */
  @Nullable
  private OnScreenChangedListener<M> mOnScreenChangedListener;

  public TriadDelegate(@NotNull final Activity activity) {
    mActivity = activity;
  }

  public void onCreate(@NotNull final Screen<?, ?, M> initialScreen,
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

  public void onStart() {
    if (mTriadPresenter == null) {
      throw new IllegalStateException("TriadPresenter is null. Make sure to call TriadDelegate.onCreate(Screen<?,?,M>, M).");
    }

    mTriadPresenter.onStart();
  }

  public void onPostCreate() {
    if (mTriadPresenter == null) {
      throw new IllegalStateException("TriadPresenter is null. Make sure to call TriadDelegate.onCreate(Screen<?,?,M>, M).");
    }
    if (mFlow == null) {
      throw new IllegalStateException("Flow is null. Make sure to call TriadDelegate.onCreate(Screen<?,?,M>, M).");
    }

    Backstack backstack = mFlow.getBackstack();
    AbstractList<Screen<?, ?, M>> screens = new ArrayList<>();

    for (Iterator<Backstack.Entry> iterator1 = backstack.reverseIterator(); iterator1.hasNext(); ) {
      Screen<?, ?, M> screen = (Screen<?, ?, M>) iterator1.next().getScreen();
      screens.clear();
      screens.add(screen); // TODO: This isn't very efficient, is it?
    }

    Screen lastScreen = null;
    for (Screen screen : screens) {
      mTriadPresenter.showScreen(screen, Flow.Direction.FORWARD);
      lastScreen = screen;
    }
    if (lastScreen != null) {
      onScreenChanged(lastScreen);
    }
  }

  public void onStop() {
    if (mTriadPresenter == null) {
      throw new IllegalStateException("TriadPresenter is null. Make sure to call TriadDelegate.onCreate(Screen<?,?,M>, M).");
    }
    mTriadPresenter.onStop();
  }

  public boolean onBackPressed() {
    if (mTriadPresenter == null) {
      throw new IllegalStateException("TriadPresenter is null. Make sure to call TriadDelegate.onCreate(Screen<?,?,M>, M).");
    }
    return mTriadPresenter.onBackPressed();
  }

  /**
   * Returns the {@link Flow} instance to be used to navigate between {@link Screen}s.
   */
  @NotNull
  public Flow getFlow() {
    if (mFlow == null) {
      throw new IllegalStateException("Flow is null. Make sure to call TriadDelegate.onCreate(Screen<?,?,M>, M).");
    }
    return mFlow;
  }

  /**
   * Sets an {@link OnScreenChangedListener} to be notified of screen changes.
   */
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
        throw new IllegalStateException("TriadPresenter is null. Make sure to call TriadDelegate.onCreate(Screen<?,?,M>, M).");
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
