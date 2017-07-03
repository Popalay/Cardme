package com.popalay.cardme.business.splash;

import android.util.Log;

import com.popalay.cardme.data.repositories.settings.SettingsRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class SplashInteractor {

    private final SettingsRepository settingsRepository;

    @Inject SplashInteractor(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public Completable start() {
        return Completable.mergeArray(Completable.timer(1, TimeUnit.SECONDS),
                settingsRepository.hasSettings()
                        .doOnSuccess(aBoolean -> Log.d("ss", "start: " + aBoolean))
                        .flatMapCompletable(hasSettings -> hasSettings ? Completable.complete()
                                : settingsRepository.save(settingsRepository.createDefault())))
                .subscribeOn(Schedulers.io());
    }
}
