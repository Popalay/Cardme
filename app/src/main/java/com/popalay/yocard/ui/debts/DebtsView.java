package com.popalay.yocard.ui.debts;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.ui.base.BaseView;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface DebtsView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showAddDialog();

    void setDebts(List<Debt> items);
}
