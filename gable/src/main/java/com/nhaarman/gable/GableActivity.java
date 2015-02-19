package com.nhaarman.gable;

import android.app.Activity;
import android.os.Bundle;
import com.nhaarman.gable.container.ScreenContainer;
import com.nhaarman.gable.presenter.ScreenPresenter;
import com.nhaarman.gable.screen.Screen;
import flow.Backstack;
import flow.Flow;
import org.jetbrains.annotations.NotNull;

public abstract class GableActivity<M> extends Activity {

  @NotNull
  private Flow mFlow;

  @NotNull
  private GablePresenter<M> mGablePresenter;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    FlowManager flowManager = FlowManager.getInstance(Backstack.single(createInitialScreen()));
    flowManager.setFlowListener(new MyFlowListener());
    mFlow = flowManager.getFlow();

    mGablePresenter = new GablePresenter(createMainComponent(), mFlow);

    GableView<M> flowView = new GableView(this);
    flowView.setPresenter(mGablePresenter);
    setContentView(flowView);
  }

  @NotNull
  protected abstract M createMainComponent();

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

  @NotNull
  public Flow getFlow() {
    return mFlow;
  }

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
