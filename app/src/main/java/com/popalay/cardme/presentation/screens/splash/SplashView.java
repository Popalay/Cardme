package com.popalay.cardme.presentation.screens.splash;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.presentation.base.BaseView;

public interface SplashView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void openHome();
}
