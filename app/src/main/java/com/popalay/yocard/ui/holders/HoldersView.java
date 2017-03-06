package com.popalay.yocard.ui.holders;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.yocard.data.models.Holder;
import com.popalay.yocard.ui.base.BaseView;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface HoldersView extends BaseView {

    void setHolders(List<Holder> cards);

    void openHolderCards(Holder holder);

    interface HolderListener {

        void onHolderClick(Holder holder);
    }
}
