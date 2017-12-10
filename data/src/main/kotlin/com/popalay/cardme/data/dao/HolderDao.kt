package com.popalay.cardme.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import io.reactivex.Single
import com.popalay.cardme.data.model.Holder as DataHolder

@Dao
interface HolderDao {

    @Query("SELECT * FROM holders " +
            "WHERE name = :name " +
            "LIMIT 1")
    fun get(name: String): Flowable<DataHolder>

    @Query("SELECT * FROM holders " +
            "ORDER BY name")
    fun getAll(): Flowable<List<DataHolder>>

    @Query("SELECT * FROM holders " +
            "WHERE isTrash = 1 " +
            "ORDER BY name")
    fun getAllTrashed(): Flowable<List<DataHolder>>

    @Query("SELECT COUNT (*) FROM holders " +
            "WHERE isTrash = 0 AND name = :name")
    fun getCount(name: String): Single<Int>

    @Query("SELECT * FROM holders " +
            "WHERE isTrash = 0 " +
            "ORDER BY name")
    fun getAllNotTrashed(): Flowable<List<DataHolder>>

    @Insert(onConflict = IGNORE)
    fun insertOrUpdate(holder: DataHolder)

    @Delete
    fun deleteAll(holders: List<DataHolder>)

    @Delete
    fun delete(holder: DataHolder)

}