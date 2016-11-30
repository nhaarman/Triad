package com.nhaarman.triad


abstract class KScreen<ApplicationComponent> : Screen<ApplicationComponent>() {

    abstract val layoutResourceId: Int
    override final fun getLayoutResId() = layoutResourceId

    override final fun createPresenter(viewId: Int): Presenter<*, *> {
        return applicationComponent.createPresenter(viewId)
    }

    abstract fun ApplicationComponent.createPresenter(viewId: Int): Presenter<*, *>
}
