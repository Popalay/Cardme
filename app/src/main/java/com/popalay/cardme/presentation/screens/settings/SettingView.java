package com.popalay.cardme.presentation.screens.settings;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.presentation.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SettingView extends BaseView {

    void setSettings(SettingsViewModel vm);
}
