package com.popalay.cardme.ui.settings;

import android.databinding.ObservableField;

import com.popalay.cardme.data.models.Settings;

public class SettingsViewModel {

    public final ObservableField<Settings> settings = new ObservableField<>();

    public void setSettings(Settings settings) {
        this.settings.set(settings);
    }

    public Settings getSettings() {
        return settings.get();
    }
}
