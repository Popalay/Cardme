package com.popalay.cardme.ui.settings;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.ui.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SettingView extends BaseView {

    void setSettings(SettingsViewModel vm);
}
