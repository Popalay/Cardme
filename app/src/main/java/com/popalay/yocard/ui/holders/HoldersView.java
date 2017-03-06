package com.popalay.yocard.ui.holders;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.yocard.ui.base.BaseView;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface HoldersView extends BaseView {

    void setHolders(List<String> cards);

    void openHolderCards(String holderName);

    interface HolderListener {

        void onHolderClick(String holder);
    }
}
