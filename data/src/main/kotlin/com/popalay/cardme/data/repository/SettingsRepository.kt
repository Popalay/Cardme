package com.popalay.cardme.data.repository

import com.popalay.cardme.data.dao.SettingsDao
import com.popalay.cardme.data.toData
import com.popalay.cardme.data.toDomain
import com.popalay.cardme.domain.model.Settings
import com.popalay.cardme.domain.repository.SettingsRepository
import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

@Reusable
class SettingsRepository @Inject constructor(
        private val settingsDao: SettingsDao
) : SettingsRepository {

    override fun listen(): Flowable<Settings> = settingsDao.get()
            .map { it.toDomain() }

    override fun hasSettings(): Single<Boolean> = settingsDao.count().map { it > 0 }

    override fun save(settings: Settings): Completable = Completable.fromAction {
        settingsDao.insertOrUpdate(settings.toData())
    }

    override fun createDefault() = Settings(
            theme = "Default",
            language = "English",
            //language = Locale.getDefault().displayLanguage,
            isCardBackground = true
    )
}
