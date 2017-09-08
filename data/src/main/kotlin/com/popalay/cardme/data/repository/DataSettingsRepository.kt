package com.popalay.cardme.data.repository

import com.popalay.cardme.domain.model.Settings
import com.popalay.cardme.domain.repository.SettingsRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSettingsRepository @Inject constructor(
) : SettingsRepository {

    override fun listen(): Flowable<Settings> = Flowable.empty()/*RxRealm.listenElement { it.where(DataSettings::class.java).findAll() }*/

    override fun hasSettings(): Single<Boolean> = Single.just(true)/*RxRealm.getElement { it.where(DataSettings::class.java).findFirst() }
            .map { true }
            .toSingle(false)*/

    override fun save(settings: Settings): Completable = Completable.complete()/*RxRealm.doTransactional { it.copyToRealmOrUpdate(settings) }*/

    override fun createDefault() = Settings(
            id = UUID.randomUUID().timestamp(),
            theme = "Default",
            language = "English",
            //language = Locale.getDefault().displayLanguage,
            isCardBackground = true
    )
}
