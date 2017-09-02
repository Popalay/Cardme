package com.popalay.cardme.data.dao

import android.arch.persistence.room.*
import com.popalay.cardme.data.models.Card
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(card: Card)

    @Query("SELECT * FROM cards WHERE number = :number")
    fun get(number: String): Flowable<Card>

    @Query("SELECT * FROM cards WHERE isTrash = 0 ORDER BY position, holderName")
    fun getAllNotTrashed(): Flowable<List<Card>>

    @Query("SELECT * FROM cards WHERE isTrash = 1 ORDER BY position, holderName")
    fun getAllTrashed(): Flowable<List<Card>>

    @Query("SELECT COUNT (*) FROM cards WHERE isTrash = 0 AND number = :number")
    fun cardsNotTrashedCount(number: String): Maybe<Int>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(cards: List<Card>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(card: Card)

    @Delete
    fun deleteAll(cards: List<Card>)

    @Delete
    fun delete(card: Card)

}