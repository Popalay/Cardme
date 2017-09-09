package com.popalay.cardme.domain.repository

import com.popalay.cardme.domain.model.Card
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface CardRepository {

    fun save(card: Card): Completable

    fun get(cardNumber: String): Flowable<Card>

    fun update(cards: List<Card>): Completable

    fun getAll(): Flowable<List<Card>>

    fun getAllTrashed(): Flowable<List<Card>>

    fun markAsTrash(card: Card): Completable

    fun removeTrashed(): Completable

    fun restore(card: Card): Completable

    fun cardIsNew(cardNumber: String): Single<Boolean>

    fun toJson(card: Card): Single<String>

    fun fromJson(source: String): Single<Card>
}
