package com.popalay.cardme.data.dao

import android.arch.persistence.room.*
import com.popalay.cardme.data.model.DataCard
import com.popalay.cardme.domain.model.Card
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(card: DataCard)

    @Query("SELECT * FROM cards " +
            "INNER JOIN holders ON holders.name = holderName " +
            "WHERE number = :number")
    fun get(number: String): Flowable<Card>

    @Query("SELECT * FROM cards " +
            "INNER JOIN holders ON holders.name = holderName " +
            "WHERE isTrash = 0 " +
            "ORDER BY position, holderName")
    fun getAllNotTrashed(): Flowable<List<Card>>

    @Query("SELECT * FROM cards " +
            "INNER JOIN holders ON holders.name = holderName " +
            "WHERE isTrash = 1 " +
            "ORDER BY position, holderName")
    fun getAllTrashed(): Flowable<List<Card>>

    @Query("SELECT COUNT (*) FROM cards WHERE isTrash = 0 AND number = :number")
    fun cardsNotTrashedCount(number: String): Maybe<Int>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(cards: List<DataCard>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(card: DataCard)

    @Delete
    fun deleteAll(cards: List<DataCard>)

    @Delete
    fun delete(card: DataCard)

}