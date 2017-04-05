package com.popalay.yocard.ui.adddebt;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.yocard.ui.base.BaseView;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface AddDebtView extends BaseView {

    @StateStrategyType(SingleStateStrategy.class)
    void setViewModel(AddDebtViewModel viewModel);

    void setCompletedCardHolders(List<String> holders);

}
