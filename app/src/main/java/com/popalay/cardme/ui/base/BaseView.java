package com.popalay.cardme.ui.base;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface BaseView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showError(String error);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMessage(String message);

    void showProgress();

    void hideProgress();

    void hideKeyboard();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void close();
}
