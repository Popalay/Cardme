package com.popalay.cardme.presentation.screens.addcard;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.presentation.base.BaseView;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface AddCardView extends BaseView {

    void showCardDetails(AddCardViewModel vm);

    void setCompletedCardHolders(List<String> holders);

}
