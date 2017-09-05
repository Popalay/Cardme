package com.popalay.cardme.domain.repository

import com.popalay.cardme.domain.model.Settings
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface SettingsRepository {

    fun listen(): Flowable<Settings>

    fun hasSettings(): Single<Boolean>

    fun save(settings: Settings): Completable

    fun createDefault(): Settings
}
