package com.popalay.cardme.presentation.base

import android.support.annotation.StringRes

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.popalay.cardme.utils.AddToEndSingleByTagStateStrategy

interface BaseView : MvpView {

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy::class, tag = "Error")
    fun showError(error: String)

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy::class, tag = "Error")
    fun showError(@StringRes error: Int)

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy::class, tag = "Message")
    fun showMessage(message: String)

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy::class, tag = "Error")
    fun hideError()

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy::class, tag = "Message")
    fun hideMessage()

    fun showProgress()

    fun hideProgress()

    fun hideKeyboard()

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun close()
}
