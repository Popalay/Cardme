package com.popalay.yocard.ui.cards;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.yocard.ui.base.BaseView;

import io.card.payment.CreditCard;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CardsView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void startCardScanning();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void addCardDetails(CreditCard card);
}
