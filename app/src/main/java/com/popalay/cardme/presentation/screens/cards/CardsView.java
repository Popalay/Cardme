package com.popalay.cardme.presentation.screens.cards;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.presentation.screens.removablelistitem.RemovableListItemView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CardsView extends RemovableListItemView<Card> {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void startCardScanning();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void addCardDetails();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void shareCardNumber(String cardNumber);

    void setViewModel(CardsViewModel viewModel);

}
