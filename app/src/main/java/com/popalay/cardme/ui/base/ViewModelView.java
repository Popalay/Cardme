package com.popalay.cardme.ui.base;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface ViewModelView<M extends BaseViewModel> extends BaseView {

    @StateStrategyType(SingleStateStrategy.class)
    void setViewModel(M viewModel);
}
