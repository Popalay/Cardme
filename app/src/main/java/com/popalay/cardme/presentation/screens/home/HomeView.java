package com.popalay.cardme.presentation.screens.home;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.presentation.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface HomeView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void openPolicy();
}
