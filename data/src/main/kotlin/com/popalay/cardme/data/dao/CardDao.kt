package com.popalay.cardme.data.dao

import android.arch.persistence.room.*
import com.popalay.cardme.data.model.DataCard
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(card: DataCard)

    @Query("SELECT * FROM cards WHERE number = :number")
    fun get(number: String): Flowable<DataCard>

    @Query("SELECT * FROM cards WHERE isTrash = 0 ORDER BY position, holderName")
    fun getAllNotTrashed(): Flowable<List<DataCard>>

    @Query("SELECT * FROM cards WHERE isTrash = 1 ORDER BY position, holderName")
    fun getAllTrashed(): Flowable<List<DataCard>>

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