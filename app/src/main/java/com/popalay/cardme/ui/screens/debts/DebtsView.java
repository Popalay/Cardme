package com.popalay.cardme.ui.screens.debts;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.ui.screens.removablelistitem.RemovableListItemView;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface DebtsView extends RemovableListItemView<Debt> {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showAddDialog();

    void setItems(List<Debt> items);
}
