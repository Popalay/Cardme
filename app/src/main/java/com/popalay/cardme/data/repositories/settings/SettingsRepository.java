package com.popalay.cardme.data.repositories.settings;

import com.popalay.cardme.data.models.Settings;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface SettingsRepository {

    Observable<Settings> listen();

    Single<Settings> get();

    Completable save(Settings settings);
}
