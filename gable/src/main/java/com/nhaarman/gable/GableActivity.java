package com.nhaarman.gable;

import android.app.Activity;
import android.os.Bundle;
import com.nhaarman.gable.container.ScreenContainer;
import com.nhaarman.gable.presenter.Presenter;
import com.nhaarman.gable.presenter.ScreenPresenter;
import com.nhaarman.gable.screen.Screen;
import flow.Backstack;
import flow.Flow;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link Activity} which is the root of an application that uses Gable.
 *
 * @param <M> The {@code main component} to use for {@link Presenter} creation.
 */
public abstract class GableActivity<M> extends Activity {

  /**
   * The {@link Flow} instance that is used to navigate between {@link Screen}s.
   */
  @NotNull
  private Flow mFlow;

  /**
   * The {@link GablePresenter} that handles {@link Screen} navigation.
   */
  @NotNull
  private GablePresenter<M> mGablePresenter;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initializeFlow();
    initializePresenter();
    initializeView();
  }

  /**
   * Retrieves the {@link Flow} instance, and registers this {@code GableActivity} to be notified of screen transitions.
   */
  private void initializeFlow() {
    FlowManager flowManager = FlowManager.getInstance(Backstack.single(createInitialScreen()));
    flowManager.setFlowListener(new MyFlowListener());
    mFlow = flowManager.getFlow();
  }

  /**
   * Creates the {@link GablePresenter}.
   */
  private void initializePresenter() {
    mGablePresenter = new GablePresenter(createMainComponent(), mFlow);
  }

  /**
   * Initializes the {@link GableView}.
   */
  private void initializeView() {
    GableView<M> flowView = new GableView(this);
    flowView.setPresenter(mGablePresenter);
    setContentView(flowView);
  }

  /**
   * Creates the main component which is used to retrieve dependencies from that are needed to create {@link Presenter}s.
   *
   * @return The created main component.
   */
  @NotNull
  protected abstract M createMainComponent();

  /**
   * Creates the {@link Screen} that is to be shown when the application starts.
   *
   * @return The created {@link Screen}.
   */
  @NotNull
  protected abstract Screen<?, ?, M> createInitialScreen();

  @Override
  protected void onStart() {
    super.onStart();
    mFlow.resetTo(mFlow.getBackstack().current().getScreen());
  }

  @Override
  public void onBackPressed() {
    if (!mGablePresenter.onBackPressed()) {
      super.onBackPressed();
    }
  }

  /**
   * Returns the {@link Flow} instance to be used to navigate between {@link Screen}s.
   */
  @NotNull
  protected Flow getFlow() {
    return mFlow;
  }

  /**
   * A {@link Flow.Listener} that delegates {@link Screen} transitions to the {@link GablePresenter}.
   */
  private class MyFlowListener implements Flow.Listener {

    @Override
    public void go(final Backstack nextBackstack, final Flow.Direction direction, final Flow.Callback callback) {
      //noinspection rawtypes
      Screen<? extends ScreenPresenter, ? extends ScreenContainer, M> screen =
          (Screen<? extends ScreenPresenter, ? extends ScreenContainer, M>) nextBackstack.current().getScreen();
      mGablePresenter.showScreen(screen);
      callback.onComplete();
    }
  }
}
