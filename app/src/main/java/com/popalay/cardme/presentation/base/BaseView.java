package com.popalay.cardme.presentation.base;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.utils.AddToEndSingleByTagStateStrategy;

public interface BaseView extends MvpView {

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy.class, tag = "Error")
    void showError(String error);

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy.class, tag = "Error")
    void showError(@StringRes int error);

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy.class, tag = "Message")
    void showMessage(String message);

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy.class, tag = "Error")
    void hideError();

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy.class, tag = "Message")
    void hideMessage();

    void showProgress();

    void hideProgress();

    void hideKeyboard();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void close();
}
