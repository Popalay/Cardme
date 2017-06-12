package com.popalay.cardme.ui.screens.addcard;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.ui.base.BaseView;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface AddCardView extends BaseView {

    void showCardDetails(AddCardViewModel vm);

    void setCompletedCardHolders(List<String> holders);

}
