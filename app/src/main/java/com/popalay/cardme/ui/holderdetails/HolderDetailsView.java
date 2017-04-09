package com.popalay.cardme.ui.holderdetails;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.ui.base.BaseView;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface HolderDetailsView extends BaseView {

    void setHolderName(String name);

    void setCards(List<Card> cards);

    void setDebts(List<Debt> debts);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void shareCardNumber(String number);
}
