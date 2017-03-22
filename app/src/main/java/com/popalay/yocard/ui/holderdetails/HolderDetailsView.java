package com.popalay.yocard.ui.holderdetails;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.ui.base.BaseView;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface HolderDetailsView extends BaseView {

    void setHolderName(String name);

    void setCards(List<Card> cards);

    void setDebts(List<Debt> debts);

    void removeCard(int position);

    void resetCard(Card card, int position);

    void showRemoveUndoAction(Card card, int position);

}
