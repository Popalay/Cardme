package com.popalay.cardme.data.dao

import android.arch.persistence.room.*
import com.popalay.cardme.data.model.DataCard
import com.popalay.cardme.domain.model.Card
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface CardDao {

    @Query("SELECT * FROM cards " +
            "WHERE number = :number")
    fun get(number: String): Flowable<Card>

    @Query("SELECT * FROM cards " +
            "WHERE isTrash = 0 " +
            "ORDER BY position, holderName")
    fun getAllNotTrashed(): Flowable<List<Card>>

    @Query("SELECT * FROM cards " +
            "WHERE isTrash = 1 " +
            "ORDER BY position, holderName")
    fun getAllTrashed(): Flowable<List<Card>>

    @Query("SELECT COUNT (*) FROM cards " +
            "WHERE isTrash = 0 AND number = :number")
    fun cardsNotTrashedCount(number: String): Maybe<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(card: DataCard)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(card: DataCard)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(vararg cards: DataCard)

    @Delete
    fun deleteAll(vararg cards: DataCard)

    @Delete
    fun delete(card: DataCard)

}