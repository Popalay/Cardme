package com.popalay.cardme.data.repositories.settings;

import android.content.Context;
import android.support.annotation.NonNull;

import com.popalay.cardme.data.models.Settings;
import com.popalay.cardme.utils.RxRealm;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Singleton
public class SettingsRepository {

    private final Context context;

    @Inject SettingsRepository(Context context) {this.context = context;}

    public Flowable<Settings> listen() {
        return RxRealm.listenElement(realm -> realm.where(Settings.class).findAll())
                .defaultIfEmpty(createDefault());
    }

    public Completable save(@NonNull Settings settings) {
        return RxRealm.doTransactional(realm -> realm.copyToRealmOrUpdate(settings));
    }

    public Settings createDefault() {
        return new Settings.Builder()
                .theme("Default")
                .language(context.getResources().getConfiguration().locale.getDisplayLanguage())
                .cardBackground(true)
                .build();
    }
}
