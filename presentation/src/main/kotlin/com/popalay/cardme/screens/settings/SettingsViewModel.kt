package com.popalay.cardme.screens.settings

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.popalay.cardme.domain.interactor.SettingsInteractor
import com.popalay.cardme.domain.model.Settings
import com.popalay.cardme.base.BaseViewModel
import com.stepango.rxdatabindings.observe
import com.stepango.rxdatabindings.setTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
        private val settingsInteractor: SettingsInteractor
) : BaseViewModel() {

    val settings = ObservableField<Settings>()
    val showImages = ObservableBoolean()

    init {
        settingsInteractor.listenSettings()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(settings)
                .map { it.isCardBackground }
                .setTo(showImages)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        showImages.observe()
                .filter { settings.get() != null }
                .map { settings.get().isCardBackground = it; settings.get() }
                .flatMapCompletable(settingsInteractor::saveSettings)
                .subscribeBy()
                .addTo(disposables)
    }
}
