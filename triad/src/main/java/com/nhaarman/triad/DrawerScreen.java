package com.nhaarman.triad;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

public abstract class DrawerScreen<
    P extends ScreenPresenter<P, C>,
    C extends ScreenContainer<P, C>,
    DP extends Presenter<DP, DC>,
    DC extends Container<DP, DC>,
    M
    > extends Screen<P, C, M> {

  @Nullable
  private DP mDrawerPresenter;

  @NonNull
  protected abstract DP createDrawerPresenter(@NonNull final M component);

  /**
   * Returns the {@link P} that is tied to this {@code Screen} instance.
   * This instance is lazily instantiated.
   *
   * @param component The {@code main component} to retrieve dependencies from.
   *
   * @return The {@link P}.
   */
  @NonNull
  private DP getDrawerPresenter(@NonNull final M component) {
    if (mDrawerPresenter == null) {
      mDrawerPresenter = createDrawerPresenter(component);
    }

    return mDrawerPresenter;
  }

  @Override
  void acquirePresenter(@NonNull final M component, @NonNull final Triad triad, @NonNull final ViewGroup container) {
    DC dc = (DC) container.getChildAt(1);

    dc.setPresenter(getDrawerPresenter(component));
    super.acquirePresenter(component, triad, (ViewGroup) container.getChildAt(0));
  }
}
