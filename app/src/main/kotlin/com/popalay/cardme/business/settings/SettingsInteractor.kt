package com.popalay.cardme.business.settings

import com.popalay.cardme.data.models.Settings
import com.popalay.cardme.data.repositories.settings.SettingsRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsInteractor @Inject constructor(private val settingsRepository: SettingsRepository) {

    fun listenSettings(): Flowable<Settings> = settingsRepository.listen()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())

    fun listenShowCardsBackground(): Flowable<Boolean> = listenSettings()
            .map { it.isCardBackground }
            .distinctUntilChanged()

    fun saveSettings(settings: Settings): Completable = settingsRepository.save(settings)
            .subscribeOn(Schedulers.io())

}
