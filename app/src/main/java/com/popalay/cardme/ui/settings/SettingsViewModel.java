package com.popalay.cardme.ui.settings;

import android.databinding.ObservableField;

import com.popalay.cardme.data.models.Settings;

public class SettingsViewModel {

    public final ObservableField<Settings> settings;

    public SettingsViewModel(Settings settings) {
        this.settings = new ObservableField<>(settings);
    }

    public void setSettings(Settings settings) {
        this.settings.set(settings);
        this.settings.notifyChange();
    }

    public Settings getSettings() {
        return settings.get();
    }
}
