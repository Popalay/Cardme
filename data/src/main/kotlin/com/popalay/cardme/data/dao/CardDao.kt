package com.popalay.cardme.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.popalay.cardme.data.model.Card
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CardDao : BaseDao<Card> {

    @Query("SELECT * FROM cards " +
            "WHERE isPending = 0 " +
            "ORDER BY position, holderName")
    fun getAll(): Flowable<List<Card>>

    @Query("SELECT * FROM cards " +
            "WHERE number = :number " +
            "LIMIT 1")
    fun get(number: String): Flowable<Card>

    @Query("SELECT * FROM cards " +
            "WHERE isTrash = 0 AND isPending = 0 " +
            "ORDER BY position, holderName")
    fun getAllNotTrashed(): Flowable<List<Card>>

    @Query("SELECT * FROM cards " +
            "WHERE isTrash = 0 AND isPending = 0 AND holderName = :holderName " +
            "ORDER BY position, holderName")
    fun getNotTrashedByHolder(holderName: String): Flowable<List<Card>>

    @Query("SELECT * FROM cards " +
            "WHERE isTrash = 1 AND isPending = 0 " +
            "ORDER BY position, holderName")
    fun getAllTrashed(): Flowable<List<Card>>

    @Query("SELECT COUNT (*) FROM cards " +
            "WHERE isTrash = 0 AND isPending = 0 AND number = :number")
    fun getCount(number: String): Single<Int>

    @Query("SELECT COUNT (*) FROM cards " +
            "WHERE isTrash = 0 AND holderName = :holderName")
    fun getCountByHolder(holderName: String): Flowable<Int>

    @Update(onConflict = REPLACE)
    fun updateAll(cards: List<Card>)

    @Delete fun deleteAll(cards: List<Card>)
}