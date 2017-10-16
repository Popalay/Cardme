package com.popalay.cardme.data.repository

import com.google.gson.Gson
import com.popalay.cardme.data.dao.CardDao
import com.popalay.cardme.data.toData
import com.popalay.cardme.data.toDomain
import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.repository.CardRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataCardRepository @Inject constructor(
        private val cardDao: CardDao,
        private val gson: Gson
) : CardRepository {

    override fun save(card: Card): Completable = Completable.fromAction {
        cardDao.insertOrUpdate(card.toData())
    }

    override fun get(cardNumber: String): Flowable<Card> = cardDao.get(cardNumber)
            .map { it.toDomain() }

    override fun update(cards: List<Card>): Completable = Completable.fromAction {
        cardDao.updateAll(cards.map { it.toData() })
    }

    override fun getAll(): Flowable<List<Card>> = cardDao.getAllNotTrashed()
            .map { it.map { it.toDomain() } }

    override fun getAllTrashed(): Flowable<List<Card>> = cardDao.getAllTrashed()
            .map { it.map { it.toDomain() } }

    override fun markAsTrash(card: Card): Completable = Completable.complete()/*RxRealm.doTransactional {
        it.where(Card::class.java).equalTo(Card.NUMBER, card.number).findFirst().isTrash = true
    }*/

    override fun removeTrashed(): Completable = Completable.complete()/*RxRealm.doTransactional {
        it.where(Card::class.java).equalTo(Card.IS_TRASH, true).findAll().deleteAllFromRealm()
    }*/

    override fun restore(card: Card): Completable = Completable.complete()/*RxRealm.doTransactional {
        it.where(Card::class.java).equalTo(Card.NUMBER, card.number).findFirst().isTrash = false
    }*/

    override fun cardIsNew(cardNumber: String): Single<Boolean> = cardDao.cardsNotTrashedCount(cardNumber).map { it == 0 }

    override fun toJson(card: Card): Single<String> = Single.fromCallable { gson.toJson(card) }

    override fun fromJson(source: String): Single<Card> = Single.fromCallable {
        gson.fromJson<Card>(source, Card::class.java)
    }
}
