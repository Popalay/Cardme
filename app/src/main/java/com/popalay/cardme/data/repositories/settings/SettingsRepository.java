package com.popalay.cardme.data.repositories.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.cardme.data.models.Settings;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public class SettingsRepository {

    private final Context context;

    @Inject SettingsRepository(Context context) {this.context = context;}

    public Flowable<Settings> listen() {
        return RxRealm.listenElement(realm -> realm.where(Settings.class).findAll());
    }

    public Single<Boolean> hasSettings() {
        return RxRealm.getElement(realm -> realm.where(Settings.class).findFirst())
                .map(settings -> true)
                .toSingle(false);
    }

    public Completable save(@NonNull Settings settings) {
        Log.d("s", "save: " + settings.getLanguage());
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
