package com.popalay.cardme.data.repositories

import com.github.popalay.rxrealm.RxRealm
import com.popalay.cardme.data.models.Settings
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor() {

    fun listen(): Flowable<Settings> = RxRealm.listenElement { it.where(Settings::class.java).findAll() }

    fun hasSettings(): Single<Boolean> = RxRealm.getElement { it.where(Settings::class.java).findFirst() }
            .map { true }
            .toSingle(false)

    fun save(settings: Settings): Completable = RxRealm.doTransactional { it.copyToRealmOrUpdate(settings) }

    fun createDefault() = Settings(
            theme = "Default",
            language = "English",
            //language = Locale.getDefault().displayLanguage,
            isCardBackground = true
    )
}
