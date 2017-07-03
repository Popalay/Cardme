package com.popalay.cardme.data.repositories.holder

import com.github.popalay.rxrealm.RxRealm
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.data.models.Holder
import io.reactivex.Completable
import io.reactivex.Flowable
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

    fun updateCounts(holder: Holder): Completable = RxRealm.doTransactional {
        val cardCount = it.where(Card::class.java).equalTo(Card.HOLDER_ID, holder.id).count()
        val debtCount = it.where(Debt::class.java).equalTo(Debt.HOLDER_ID, holder.id).count()
        if (cardCount <= 0 && debtCount <= 0) {
            it.where(Holder::class.java).equalTo(Holder.ID, holder.id).findAll().deleteAllFromRealm()
        } else {
            holder.cardsCount = cardCount.toInt()
            holder.debtCount = debtCount.toInt()
            it.copyToRealmOrUpdate(holder)
        }
    }

    fun remove(holder: Holder): Completable = RxRealm.doTransactional {
        it.where(Holder::class.java).equalTo(Holder.ID, holder.id).findAll().deleteAllFromRealm()
    }
}