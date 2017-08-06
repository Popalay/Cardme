package com.popalay.cardme.business.splash

import com.popalay.cardme.data.repositories.settings.SettingsRepository
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashInteractor @Inject internal constructor(private val settingsRepository: SettingsRepository) {

    fun start(): Completable = settingsRepository.hasSettings()
            .flatMapCompletable {
                if (it) Completable.complete()
                else settingsRepository.save(settingsRepository.createDefault())
            }
            .subscribeOn(Schedulers.io())
}
