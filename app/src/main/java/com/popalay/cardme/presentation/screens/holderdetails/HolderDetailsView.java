package com.popalay.cardme.presentation.screens.holderdetails;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.presentation.base.ViewModelView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface HolderDetailsView extends ViewModelView<HolderDetailsViewModel> {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void shareCardNumber(String number);
}
