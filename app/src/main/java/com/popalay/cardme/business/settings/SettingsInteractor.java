package com.popalay.cardme.business.settings;

import com.popalay.cardme.data.models.Settings;
import com.popalay.cardme.data.repositories.settings.SettingsRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

@Singleton
public class SettingsInteractor {

    private final SettingsRepository settingsRepository;

    @Inject public SettingsInteractor(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public Observable<Settings> listenSettings() {
        return settingsRepository.listen().subscribeOn(Schedulers.io());
    }

    public Single<Settings> getSettings() {
        return settingsRepository.get().subscribeOn(Schedulers.io());
    }

    public Completable saveSettings(Settings settings) {
        return settingsRepository.save(settings).subscribeOn(Schedulers.io());
    }
}
