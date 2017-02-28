package com.popalay.yocard.ui.addcard;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.ui.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface AddCardView extends BaseView {

    void showCardDetails(Card card);
}
