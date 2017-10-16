package com.popalay.cardme.data.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable

import com.popalay.cardme.data.model.Holder as DataHolder

@Dao
interface HolderDao {

    @Query("SELECT * FROM holders " +
            "WHERE name = :name " +
            "LIMIT 1")
    fun get(name: String): Flowable<DataHolder>

    @Query("SELECT * FROM holders " +
            "WHERE isPending = 0 " +
            "ORDER BY name")
    fun getAll(): Flowable<List<DataHolder>>

    @Query("SELECT * FROM holders " +
            "WHERE isTrash = 1 AND isPending = 0 " +
            "ORDER BY name")
    fun getAllTrashed(): Flowable<List<DataHolder>>

    @Query("SELECT * FROM holders " +
            "WHERE isTrash = 0 AND isPending = 0 " +
            "ORDER BY name")
    fun getAllNotTrashed(): Flowable<List<DataHolder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(holder: DataHolder)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(holder: DataHolder)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(holders: List<DataHolder>)

    @Delete
    fun deleteAll(holders: List<DataHolder>)

    @Delete
    fun delete(holder: DataHolder)

}