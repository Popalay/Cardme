package com.popalay.cardme.data.repositories.card

import com.github.popalay.rxrealm.RxRealm
import com.popalay.cardme.data.models.Card
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject internal constructor() {

    fun save(card: Card): Single<Card> = RxRealm.doTransactional {
        it.insertOrUpdate(card.holder)
        it.insertOrUpdate(card)
    }.toSingleDefault(card)

    fun update(cards: List<Card>): Completable = RxRealm.doTransactional { it.copyToRealmOrUpdate(cards) }

    fun get(cardNumber: String): Flowable<Card> = RxRealm.listenElement {
        it.where(Card::class.java).equalTo(Card.NUMBER, cardNumber).findAll()
    }

    fun getAll(): Flowable<List<Card>> = RxRealm.listenList {
        it.where(Card::class.java)
                .equalTo(Card.IS_TRASH, false)
                .findAllSorted(Card.HOLDER_NAME)
                .sort(Card.POSITION)
    }

    fun getAllTrashed(): Flowable<List<Card>> = RxRealm.listenList {
        it.where(Card::class.java)
                .equalTo(Card.IS_TRASH, true)
                .findAllSorted(Card.HOLDER_NAME)
                .sort(Card.POSITION)
    }

    fun getAllByHolder(holderName: String): Flowable<List<Card>> = RxRealm.listenList {
        it.where(Card::class.java)
                .equalTo(Card.HOLDER_NAME, holderName)
                .equalTo(Card.IS_TRASH, false)
                .findAllSorted(Card.HOLDER_NAME)
                .sort(Card.POSITION)
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

    fun cardIsNew(card: Card): Single<Boolean> = RxRealm.getElement {
        it.where(Card::class.java).equalTo(Card.NUMBER, card.number).findFirst()
    }.isEmpty
}
