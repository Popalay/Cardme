package com.popalay.cardme.data.repositories.holder

import com.github.popalay.rxrealm.RxRealm
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.data.models.Holder
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.realm.Realm
import io.realm.Sort
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HolderRepository @Inject constructor() {

    fun addCard(holderName: String, card: Card): Completable = RxRealm.doTransactional {
        val holder = it.where(Holder::class.java).equalTo(Holder.NAME, holderName)
                .findFirst() ?: it.createObject(Holder::class.java, holderName)
        card.holder = holder
        val realmCard = it.copyToRealmOrUpdate(card)
        if (!holder.cards.contains(realmCard)) holder.cards.add(realmCard)

        updateTrashFlag(it)
    }

    fun addDebt(holderName: String, debt: Debt): Completable = RxRealm.doTransactional {
        val holder = it.where(Holder::class.java).equalTo(Holder.NAME, holderName)
                .findFirst() ?: it.createObject(Holder::class.java, holderName)
        debt.holder = holder
        val realmDebt = it.copyToRealmOrUpdate(debt)
        if (!holder.debts.contains(realmDebt)) holder.debts.add(realmDebt)

        updateTrashFlag(it)
    }

    fun removeCard(card: Card): Completable = RxRealm.doTransactional {
        val holder = it.where(Holder::class.java).equalTo(Holder.NAME, card.holder.name).findFirst()
        val realmCard = it.where(Card::class.java).equalTo(Card.NUMBER, card.number).findFirst()
        holder.cards.remove(realmCard)

        updateTrashFlag(it)
    }

    fun removeDebt(debt: Debt): Completable = RxRealm.doTransactional {
        val holder = it.where(Holder::class.java).equalTo(Holder.NAME, debt.holder.name).findFirst()
        val realmDebt = it.where(Debt::class.java).equalTo(Debt.ID, debt.id).findFirst()
        holder.debts.remove(realmDebt)

        updateTrashFlag(it)
    }

    fun getAll(): Flowable<List<Holder>> = RxRealm.listenList {
        it.where(Holder::class.java)
                .equalTo(Holder.IS_TRASH, false)
                .findAllSorted(Holder.NAME)
    }

    fun get(holderName: String): Flowable<Holder> = RxRealm.listenElement {
        it.where(Holder::class.java).equalTo(Holder.NAME, holderName).findAll()
    }

    fun getWithMaxCounters(): Maybe<Holder> = RxRealm.getElement {
        it.where(Holder::class.java)
                .findAllSorted(Holder.CARDS_COUNT, Sort.DESCENDING, Holder.DEBTS_COUNT, Sort.DESCENDING)
                .first()
    }

    fun removeTrashed(): Completable = RxRealm.doTransactional {
        it.where(Holder::class.java).equalTo(Holder.IS_TRASH, true).findAll().deleteAllFromRealm()
    }

    private fun updateTrashFlag(realm: Realm) {
        val intoTrash = realm.where(Holder::class.java)
                .isEmpty(Holder.CARDS).isEmpty(Holder.DEBTS)
                .findAll()

        for (item in intoTrash) {
            item.isTrash = true
        }

        val fromTrash = realm.where(Holder::class.java)
                .isNotEmpty(Holder.CARDS).or().isNotEmpty(Holder.DEBTS)
                .findAll()

        for (item in fromTrash) {
            item.isTrash = false
        }
    }
}