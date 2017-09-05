package com.popalay.cardme.domain.repository

import com.popalay.cardme.domain.model.Debt
import io.reactivex.Completable
import io.reactivex.Flowable

interface DebtRepository {

    fun getAll(): Flowable<List<Debt>>

    fun markAsTrash(debt: Debt): Completable

    fun restore(debt: Debt): Completable

    fun removeTrashed(): Completable
}
