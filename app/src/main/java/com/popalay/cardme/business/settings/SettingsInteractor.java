package com.popalay.cardme.business.settings;

import com.popalay.cardme.data.models.Settings;
import com.popalay.cardme.data.repositories.settings.SettingsRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class SettingsInteractor {

    private final SettingsRepository settingsRepository;

    @Inject public SettingsInteractor(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public Flowable<Settings> listenSettings() {
        return settingsRepository.listen()
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io());
    }

    public Flowable<Boolean> listenShowCardsBackground() {
        return listenSettings()
                .map(Settings::isCardBackground)
                .distinctUntilChanged();
    }

    public Completable saveSettings(Settings settings) {
        return settingsRepository.save(settings).subscribeOn(Schedulers.io());
    }
}
