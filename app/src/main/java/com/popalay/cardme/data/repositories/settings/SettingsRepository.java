package com.popalay.cardme.data.repositories.settings;

import com.popalay.cardme.data.models.Settings;

import rx.Completable;
import rx.Observable;

public interface SettingsRepository {

    Observable<Settings> listen();

    Completable saveSettings(Settings settings);
}
