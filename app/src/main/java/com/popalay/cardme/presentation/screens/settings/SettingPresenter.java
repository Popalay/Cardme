package com.popalay.cardme.presentation.screens.settings;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.settings.SettingsInteractor;
import com.popalay.cardme.presentation.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class SettingPresenter extends BasePresenter<SettingView> {

    @Inject SettingsInteractor settingsInteractor;
    private final SettingsViewModel viewModel;

    public SettingPresenter() {
        App.appComponent().inject(this);
        viewModel = new SettingsViewModel();
        getViewState().setSettings(viewModel);

        settingsInteractor.listenSettings()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel::setSettings, this::handleBaseError);
    }

    public void showImageChanged(boolean checked) {
        viewModel.getSettings().setCardBackground(checked);
        settingsInteractor.saveSettings(viewModel.getSettings())
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError);
    }
}
