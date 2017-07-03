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

    fun save(card: Card): Completable = RxRealm.doTransactional {
        if (card.id == null) {
            card.id = UUID.randomUUID().toString()
        }
        val realmHolder = it.where(Holder::class.java).equalTo(Holder.NAME, card.holder.name)
                .findFirst()
        if (realmHolder != null) {
            card.holder = realmHolder
        } else {
            card.holder.id = UUID.randomUUID().toString()
        }
        it.copyToRealmOrUpdate(card)
    }

    fun save(cards: List<Card>): Completable = RxRealm.doTransactional { it.copyToRealmOrUpdate(cards) }

    fun getAll(): Flowable<List<Card>> = RxRealm.listenList {
        it.where(Card::class.java)
                .findAllSorted(Card.ID, Sort.DESCENDING)
                .sort(Card.POSITION)
    }

    fun getAllByHolder(holderId: String): Flowable<List<Card>> = RxRealm.listenList {
        it.where(Card::class.java)
                .equalTo(Card.HOLDER_ID, holderId)
                .findAllSorted(Card.ID, Sort.DESCENDING)
                .sort(Card.POSITION)
    }

    fun remove(card: Card): Completable = RxRealm.doTransactional {
        it.where(Card::class.java).equalTo(Card.ID, card.id).findAll().deleteAllFromRealm()
    }

    fun cardIsNew(card: Card): Single<Boolean> = RxRealm.getElement {
        it.where(Card::class.java).equalTo(Card.FORMATTED_NUMBER, card.number).findFirst()
    }.isEmpty
}
