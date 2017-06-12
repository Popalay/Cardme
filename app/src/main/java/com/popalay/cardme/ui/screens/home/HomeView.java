package com.popalay.cardme.ui.screens.home;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.ui.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface HomeView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void openPage(int pageId);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void openSettings();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void openPolicy();
}
