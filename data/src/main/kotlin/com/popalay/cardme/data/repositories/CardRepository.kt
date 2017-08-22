package com.popalay.cardme.data.repositories

import com.github.popalay.rxrealm.RxRealm
import com.google.gson.Gson
import com.popalay.cardme.data.models.Card
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.realm.Sort
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
        private val gson: Gson
) {

    fun get(cardNumber: String): Flowable<Card> = RxRealm.listenElement {
        it.where(Card::class.java).equalTo(Card.NUMBER, cardNumber).findAll()
    }

    fun update(cards: List<Card>): Completable = RxRealm.doTransactional { it.copyToRealmOrUpdate(cards) }

    fun getAll(): Flowable<List<Card>> = RxRealm.listenList {
        it.where(Card::class.java)
                .equalTo(Card.IS_TRASH, false)
                .findAllSorted(Card.POSITION, Sort.ASCENDING, Card.HOLDER_NAME, Sort.ASCENDING)
    }

    fun getAllTrashed(): Flowable<List<Card>> = RxRealm.listenList {
        it.where(Card::class.java)
                .equalTo(Card.IS_TRASH, true)
                .findAllSorted(Card.POSITION, Sort.ASCENDING, Card.HOLDER_NAME, Sort.ASCENDING)
    }

    fun markAsTrash(card: Card): Completable = RxRealm.doTransactional {
        it.where(Card::class.java).equalTo(Card.NUMBER, card.number).findFirst().isTrash = true
    }

    fun removeTrashed(): Completable = RxRealm.doTransactional {
        it.where(Card::class.java).equalTo(Card.IS_TRASH, true).findAll().deleteAllFromRealm()
    }

    fun restore(card: Card): Completable = RxRealm.doTransactional {
        it.where(Card::class.java).equalTo(Card.NUMBER, card.number).findFirst().isTrash = false
    }

    fun cardIsNew(cardNumber: String): Single<Boolean> = RxRealm.getElement {
        it.where(Card::class.java)
                .equalTo(Card.IS_TRASH, false)
                .equalTo(Card.NUMBER, cardNumber).findFirst()
    }.isEmpty

    fun prepareForSharing(card: Card): Single<String> = Single.fromCallable { gson.toJson(card) }

    fun getFromJson(source: String): Single<Card> = Single.fromCallable {
        gson.fromJson<Card>(source, Card::class.java)
    }

}
