package com.popalay.cardme.presentation.screens.settings;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.settings.SettingsInteractor;
import com.popalay.cardme.presentation.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

@InjectViewState
public class SettingPresenter extends BasePresenter<SettingView> {

    @Inject SettingsInteractor settingsInteractor;
    private final SettingsViewModel viewModel;

    public SettingPresenter() {
        App.appComponent().inject(this);
        viewModel = new SettingsViewModel();
        getViewState().setSettings(viewModel);

        addDisposable(settingsInteractor.listenSettings()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(settings -> {
                    Log.d("ss", "SettingPresenter: "+ settings);
                    viewModel.setSettings(settings);
                }, this::handleBaseError));
    }

    public void showImageChanged(boolean checked) {
        addDisposable(settingsInteractor.saveSettings(viewModel.getSettings())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError));
    }
}
