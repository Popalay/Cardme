package com.popalay.cardme.presentation.screens.settings;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;

import com.popalay.cardme.data.models.Settings;
import com.popalay.cardme.utils.BindingUtils;
import com.stepango.rxdatabindings.ObservableString;

import io.reactivex.Flowable;

public class SettingsViewModel {

    private Settings settings;

    public final ObservableBoolean showImages = new ObservableBoolean();
    public final ObservableString theme = new ObservableString();
    public final ObservableString language = new ObservableString();

    public void setSettings(Settings settings) {
        this.settings = settings;
        showImages.set(settings.isCardBackground());
        theme.set(settings.getTheme());
        language.set(settings.getLanguage());

        showImages.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override public void onPropertyChanged(Observable observable, int i) {
                settings.setCardBackground(showImages.get());
            }
        });
    }

    public Settings getSettings() {
        return settings;
    }

    public Flowable<Boolean> getShowImagesObservable() {
        return BindingUtils.create(showImages)
                .distinctUntilChanged();
    }
}
