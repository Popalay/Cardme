package com.popalay.cardme.data.repositories.card

import com.github.popalay.rxrealm.RxRealm
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.models.Holder
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.realm.Sort
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject internal constructor() {

    fun save(card: Card): Single<Card> = RxRealm.doTransactional {
        if (card.id == null) {
            card.id = UUID.randomUUID().toString()
        }
        val realmHolder = it.where(Holder::class.java).equalTo(Holder.NAME, card.holder.name).findFirst()
        if (realmHolder != null) {
            card.holder = it.copyFromRealm(realmHolder)
        } else {
            card.holder.id = UUID.randomUUID().toString()
        }
        it.copyToRealmOrUpdate(card)
    }.toSingleDefault(card)

    fun update(cards: List<Card>): Completable = RxRealm.doTransactional { it.copyToRealmOrUpdate(cards) }

    fun getAll(): Flowable<List<Card>> = RxRealm.listenList {
        it.where(Card::class.java)
                .equalTo(Card.IS_TRASH, false)
                .findAllSorted(Card.ID, Sort.DESCENDING)
                .sort(Card.POSITION)
    }

    fun getAllTrashed(): Flowable<List<Card>> = RxRealm.listenList {
        it.where(Card::class.java)
                .equalTo(Card.IS_TRASH, true)
                .findAllSorted(Card.ID, Sort.DESCENDING)
                .sort(Card.POSITION)
    }

    fun getAllByHolder(holderId: String): Flowable<List<Card>> = RxRealm.listenList {
        it.where(Card::class.java)
                .equalTo(Card.HOLDER_ID, holderId)
                .equalTo(Card.IS_TRASH, false)
                .findAllSorted(Card.ID, Sort.DESCENDING)
                .sort(Card.POSITION)
    }

    fun markAsTrash(card: Card): Completable = RxRealm.doTransactional {
        it.where(Card::class.java).equalTo(Card.ID, card.id).findFirst().isTrash = true
    }

    fun removeTrashed(): Completable = RxRealm.doTransactional {
        it.where(Card::class.java).equalTo(Card.IS_TRASH, true).findAll().deleteAllFromRealm()
    }

    fun restore(card: Card): Completable = RxRealm.doTransactional {
        it.where(Card::class.java).equalTo(Card.ID, card.id).findFirst().isTrash = false
    }

    fun cardIsNew(card: Card): Single<Boolean> = RxRealm.getElement {
        it.where(Card::class.java).equalTo(Card.FORMATTED_NUMBER, card.number).findFirst()
    }.isEmpty
}
