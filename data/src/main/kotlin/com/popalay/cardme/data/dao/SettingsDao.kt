package com.popalay.cardme.data.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single

import com.popalay.cardme.data.model.Settings as DataSettings
import com.popalay.cardme.domain.model.Settings as DomainSettings

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings " +
            "LIMIT 1")
    fun get(): Flowable<DomainSettings>

    @Query("SELECT COUNT (*) FROM settings")
    fun count(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(settings: DataSettings)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(settings: DataSettings)
}