package com.popalay.yocard.ui.holdercards;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.ui.base.BaseView;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface HolderCardsView extends BaseView {

    void setHolderName(String name);

    void setCards(List<Card> cards);

    void removeCard(int position);

    void resetCard(Card card, int position);

    void showRemoveUndoAction(Card card, int position);

}
