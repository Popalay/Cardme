package com.popalay.cardme.presentation.base

abstract class ViewModelPresenter<T : ViewModelView<M>, M : BaseViewModel> : BasePresenter<T>() {

    protected val viewModel: M

    init {
        viewModel = createViewModel()
        viewState.setViewModel(viewModel)
    }

    protected  abstract fun createViewModel(): M
}
