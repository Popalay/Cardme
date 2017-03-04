package com.popalay.yocard.ui.cards;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.ui.base.BaseView;

import java.util.List;

import io.card.payment.CreditCard;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CardsView extends BaseView {

    void setCards(List<Card> cards);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void startCardScanning();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void addCardDetails(CreditCard card);

    void removeCard(int position);

    void addCard(Card card, int position);

    void showRemoveUndoAction(Card card, int position);

    interface CardListener {

        void onCardClick(Card card);
    }
}
