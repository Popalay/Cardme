package com.popalay.cardme.ui.settings;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.settings.SettingsInteractor;
import com.popalay.cardme.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class SettingPresenter extends BasePresenter<SettingView> {

    @Inject SettingsInteractor settingsInteractor;
    private SettingsViewModel vm;

    public SettingPresenter() {
        App.appComponent().inject(this);

        settingsInteractor.listenSettings()
                .compose(bindToLifecycle())
                .map(settings -> this.vm = new SettingsViewModel(settings))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setSettings, this::handleBaseError);
    }

    public void onClose() {
        settingsInteractor.saveSettings(vm.getSettings())
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError);
    }
}
