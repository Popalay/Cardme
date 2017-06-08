package com.popalay.cardme.data.repositories.settings;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.cardme.data.models.Settings;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;

public class DefaultSettingsRepository implements SettingsRepository {

    @Inject public DefaultSettingsRepository() {}

    @Override public Observable<Settings> listen() {
        return RxRealm.listenElement(realm -> realm.where(Settings.class).findAll());
    }

    @Override public Completable saveSettings(Settings settings) {
        return Completable.fromAction(() -> RxRealm.doTransactional(realm -> realm.copyToRealmOrUpdate(settings)));
    }
}
