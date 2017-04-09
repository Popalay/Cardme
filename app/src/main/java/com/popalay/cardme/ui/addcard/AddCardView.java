package com.popalay.cardme.ui.addcard;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.ui.base.BaseView;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface AddCardView extends BaseView {

    void showCardDetails(Card card);

    void setCompletedCardHolders(List<String> holders);
}
