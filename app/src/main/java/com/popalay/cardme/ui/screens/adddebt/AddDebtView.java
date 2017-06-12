package com.popalay.cardme.ui.screens.adddebt;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.ui.base.BaseView;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface AddDebtView extends BaseView {

    @StateStrategyType(SingleStateStrategy.class)
    void setViewModel(AddDebtViewModel viewModel);

    void setCompletedCardHolders(List<String> holders);

}
