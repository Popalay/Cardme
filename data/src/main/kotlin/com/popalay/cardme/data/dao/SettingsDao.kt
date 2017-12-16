package com.popalay.cardme.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import io.reactivex.Single
import com.popalay.cardme.data.model.Settings as DataSettings

@Dao
interface SettingsDao : BaseDao<DataSettings> {

    @Query("SELECT * FROM settings " +
            "LIMIT 1")
    fun get(): Flowable<DataSettings>

    @Query("SELECT COUNT (*) FROM settings")
    fun count(): Single<Int>

}