package com.popalay.yocard.ui.home;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.yocard.ui.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface HomeView extends BaseView {

    void openPage(int pageId);

}
