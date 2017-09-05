package com.popalay.cardme.domain.repository

import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.model.Debt
import com.popalay.cardme.domain.model.Holder
import io.reactivex.Completable
import io.reactivex.Flowable

interface HolderRepository {

    fun addCard(holderName: String, card: Card): Completable

    fun addDebt(holderName: String, debt: Debt): Completable

    fun removeCard(card: Card): Completable

    fun removeDebt(debt: Debt): Completable

    fun getAll(): Flowable<List<Holder>>

    fun get(holderName: String): Flowable<Holder>

    fun removeTrashed(): Completable

}