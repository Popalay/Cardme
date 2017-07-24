package com.popalay.cardme.data.repositories.holder

import com.github.popalay.rxrealm.RxRealm
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.data.models.Holder
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.realm.Sort
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HolderRepository @Inject constructor() {

    fun getAll(): Flowable<List<Holder>> = RxRealm.listenList {
        it.where(Holder::class.java).findAllSorted(Holder.NAME)
    }

    fun get(holderId: String): Flowable<Holder> = RxRealm.listenElement { realm ->
        realm.where(Holder::class.java).equalTo(Holder.ID, holderId).findAll()
    }

    fun getWithMaxCounters(): Maybe<Holder> = RxRealm.getElement { realm ->
        realm.where(Holder::class.java)
                .findAllSorted(Holder.CARDS_COUNT, Sort.DESCENDING, Holder.DEBTS_COUNT, Sort.DESCENDING)
                .first()
    }

    fun removeTrashed(): Completable = RxRealm.doTransactional {
        it.where(Holder::class.java).equalTo(Holder.IS_TRASH, true).findAll().deleteAllFromRealm()
    }

    fun updateCounts(holder: Holder): Completable = RxRealm.doTransactional {
        val cardCount = it.where(Card::class.java).equalTo(Card.IS_TRASH, false)
                .equalTo(Card.HOLDER_ID, holder.id).count()
        val debtCount = it.where(Debt::class.java).equalTo(Debt.IS_TRASH, false)
                .equalTo(Debt.HOLDER_ID, holder.id).count()
        val realmHolder = it.where(Holder::class.java).equalTo(Holder.ID, holder.id).findFirst()
        if (cardCount <= 0 && debtCount <= 0) {
            realmHolder.isTrash = true
        } else {
            realmHolder.isTrash = false
            realmHolder.cardsCount = cardCount.toInt()
            realmHolder.debtCount = debtCount.toInt()
        }
    }
}