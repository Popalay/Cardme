package com.popalay.cardme.domain.interactor

import com.popalay.cardme.domain.model.Settings
import com.popalay.cardme.domain.repository.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsInteractor @Inject constructor(
        private val deviceRepository: DeviceRepository,
        private val settingsRepository: SettingsRepository,
        private val cardRepository: CardRepository,
        private val holderRepository: HolderRepository,
        private val debtRepository: DebtRepository
) {

    fun listenSettings(): Flowable<Settings> = settingsRepository.listen()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())

    fun listenShowCardsBackground(): Flowable<Boolean> = listenSettings()
            .map { it.isCardBackground }
            .distinctUntilChanged()

    fun saveSettings(settings: Settings): Completable = settingsRepository.save(settings)
            .subscribeOn(Schedulers.io())

    fun enableNfcFeatures(): Single<Boolean> = Single.fromCallable { deviceRepository.supportNfc() }

    fun emptyTrash(): Completable = Completable.mergeArray(
            cardRepository.removeTrashed(),
            holderRepository.removeTrashed(),
            debtRepository.removeTrashed())
            .subscribeOn(Schedulers.io())

}
