package com.popalay.yocard.ui.base;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface BaseView extends MvpView {

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void showError(String error);

    void showProgress();

    void hideProgress();

    void hideKeyboard();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void close();
}
