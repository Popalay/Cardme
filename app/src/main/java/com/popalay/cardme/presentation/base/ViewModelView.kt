package com.popalay.cardme.presentation.base

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface ViewModelView<in M : BaseViewModel> : BaseView {

    @StateStrategyType(SingleStateStrategy::class)
    fun setViewModel(viewModel: M)
}
