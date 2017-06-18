package com.popalay.cardme.presentation.screens.settings;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.popalay.cardme.data.models.Settings;
import com.popalay.cardme.utils.BindingObservable;

import rx.Observable;

public class SettingsViewModel {

    public final ObservableField<Settings> settings = new ObservableField<>();
    public final ObservableBoolean showImages = new ObservableBoolean();

    public void setSettings(Settings settings) {
        this.settings.set(settings);
        showImages.set(settings.isCardBackground());
    }

    public Settings getSettings() {
        return settings.get();
    }

    public Observable<Boolean> getShowImagesObservable() {
        return BindingObservable.create(showImages);
    }
}
