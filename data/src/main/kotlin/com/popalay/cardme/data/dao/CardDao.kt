package com.popalay.cardme.data.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single

import com.popalay.cardme.data.model.Card as DataCard
import com.popalay.cardme.domain.model.Card as DomainCard

@Dao
interface CardDao {

    @Query("SELECT * FROM cards " +
            "WHERE number = :number " +
            "LIMIT 1")
    fun get(number: String): Flowable<DomainCard>

    @Query("SELECT * FROM cards " +
            "WHERE isTrash = 0 AND isPending = 0 " +
            "ORDER BY position, holderName")
    fun getAllNotTrashed(): Flowable<List<DomainCard>>

    @Query("SELECT * FROM cards " +
            "WHERE isTrash = 1 AND isPending = 0 " +
            "ORDER BY position, holderName")
    fun getAllTrashed(): Flowable<List<DomainCard>>

    @Query("SELECT COUNT (*) FROM cards " +
            "WHERE isTrash = 0 AND isPending = 0 AND number = :number")
    fun cardsNotTrashedCount(number: String): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(card: DataCard)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(card: DataCard)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(cards: List<DataCard>)

    @Delete
    fun deleteAll(cards: List<DataCard>)

    @Delete
    fun delete(card: DataCard)

}