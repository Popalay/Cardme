package com.popalay.cardme.presentation.base;

public abstract class ViewModelPresenter<T extends ViewModelView<M>,
        M extends BaseViewModel> extends BasePresenter<T> {

    protected final M viewModel;

    public ViewModelPresenter() {
        viewModel = createViewModel();
        getViewState().setViewModel(viewModel);
    }

    protected abstract M createViewModel();
}
