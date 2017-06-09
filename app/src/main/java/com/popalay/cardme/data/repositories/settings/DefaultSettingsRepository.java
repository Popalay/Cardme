package com.popalay.cardme.data.repositories.settings;

import android.content.Context;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.cardme.data.models.Settings;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class DefaultSettingsRepository implements SettingsRepository {

    private final Context context;

    @Inject public DefaultSettingsRepository(Context context) {this.context = context;}

    @Override public Observable<Settings> listen() {
        return RxRealm.listenElement(realm -> realm.where(Settings.class).findAll())
                .map(settings -> settings != null ? settings : createDefault());
    }

    @Override public Single<Settings> get() {
        return RxRealm.getElement(realm -> realm.where(Settings.class).findFirst())
                .map(settings -> settings != null ? settings : createDefault());
    }

    @Override public Completable save(Settings settings) {
        return Completable.fromAction(() -> RxRealm.doTransactional(realm -> {
            realm.where(Settings.class).findAll().deleteAllFromRealm();
            realm.copyToRealmOrUpdate(settings);
        }));
    }

    private Settings createDefault() {
        return new Settings.Builder()
                .theme("Default")
                .language(context.getResources().getConfiguration().locale.getDisplayLanguage())
                .cardBackground(true)
                .build();
    }
}
