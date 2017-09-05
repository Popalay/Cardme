package com.popalay.cardme.data.repository

import com.github.popalay.rxrealm.RxRealm
import com.popalay.cardme.data.model.DataSettings
import com.popalay.cardme.domain.model.Settings
import com.popalay.cardme.domain.repository.SettingsRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSettingsRepository @Inject constructor(
) : SettingsRepository {

    override fun listen(): Flowable<Settings> = RxRealm.listenElement { it.where(DataSettings::class.java).findAll() }

    override fun hasSettings(): Single<Boolean> = RxRealm.getElement { it.where(DataSettings::class.java).findFirst() }
            .map { true }
            .toSingle(false)

    override fun save(settings: Settings): Completable = RxRealm.doTransactional { it.copyToRealmOrUpdate(settings) }

    override fun createDefault() = Settings(
            theme = "Default",
            language = "English",
            //language = Locale.getDefault().displayLanguage,
            isCardBackground = true
    )
}
