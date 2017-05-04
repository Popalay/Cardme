package com.popalay.cardme.utils;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

import java.util.Iterator;
import java.util.List;

public class AddToEndSingleByTagStateStrategy implements StateStrategy {

    @Override
    public <View extends MvpView> void beforeApply(List<ViewCommand<View>> currentState,
            ViewCommand<View> incomingCommand) {
        final Iterator<ViewCommand<View>> iterator = currentState.iterator();

        while (iterator.hasNext()) {
            final ViewCommand<View> entry = iterator.next();

            if (entry.getTag().equals(incomingCommand.getTag())) {
                iterator.remove();
                break;
            }
        }

        currentState.add(incomingCommand);
    }

    @Override
    public <View extends MvpView> void afterApply(List<ViewCommand<View>> currentState,
            ViewCommand<View> incomingCommand) {
        // pass
    }
}