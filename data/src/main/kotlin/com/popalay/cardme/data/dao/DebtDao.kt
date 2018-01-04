package com.popalay.cardme.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.popalay.cardme.data.model.Debt
import io.reactivex.Flowable

@Dao
interface DebtDao : BaseDao<Debt> {

    @Query("SELECT * FROM debts " +
            "WHERE id = :id " +
            "LIMIT 1")
    fun get(id: Int): Flowable<Debt>

    @Query("SELECT * FROM debts " +
            "ORDER BY createdAt")
    fun getAll(): Flowable<List<Debt>>

    @Query("SELECT * FROM debts " +
            "WHERE isTrash = 0 " +
            "ORDER BY createdAt")
    fun getAllNotTrashed(): Flowable<List<Debt>>

    @Query("SELECT * FROM debts " +
            "WHERE isTrash = 1 " +
            "ORDER BY createdAt")
    fun getAllTrashed(): Flowable<List<Debt>>

    @Query("SELECT * FROM debts " +
            "WHERE isTrash = 0 AND holderName = :holderName " +
            "ORDER BY createdAt")
    fun getNotTrashedByHolder(holderName: String): Flowable<List<Debt>>

    @Query("SELECT COUNT (*) FROM debts " +
            "WHERE isTrash = 0 AND holderName = :holderName")
    fun getCountByHolder(holderName: String): Flowable<Int>

    @Update(onConflict = REPLACE)
    fun updateAll(debts: List<Debt>)

    @Delete fun deleteAll(debts: List<Debt>)
}