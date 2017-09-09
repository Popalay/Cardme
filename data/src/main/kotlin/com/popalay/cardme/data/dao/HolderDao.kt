package com.popalay.cardme.data.dao

import android.arch.persistence.room.*
import com.popalay.cardme.data.model.DataHolder
import com.popalay.cardme.domain.model.Holder
import io.reactivex.Flowable

@Dao
interface HolderDao {

    @Query("SELECT * FROM holders " +
            "WHERE name = :name " +
            "LIMIT 1")
    fun get(name: String): Flowable<Holder>

    @Query("SELECT * FROM holders " +
            "WHERE isTrash = 0 AND isPending = 0 " +
            "ORDER BY name")
    fun getAllNotTrashed(): Flowable<List<Holder>>

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