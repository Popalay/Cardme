package com.popalay.cardme.presentation.screens.settings;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.popalay.cardme.data.models.Settings;
import com.popalay.cardme.utils.BindingUtils;

import io.reactivex.Flowable;

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

    public Flowable<Boolean> getShowImagesObservable() {
        return BindingUtils.create(showImages)
                .distinctUntilChanged();
    }

    public void onShowImageChecked(boolean checked) {
        settings.get().setCardBackground(checked);
        showImages.set(checked);
    }
}
