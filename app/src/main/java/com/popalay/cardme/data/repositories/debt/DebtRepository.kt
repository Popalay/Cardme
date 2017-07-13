package com.popalay.cardme.data.repositories.debt

import com.github.popalay.rxrealm.RxRealm
import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.data.models.Holder
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtRepository @Inject internal constructor() {

    fun save(debt: Debt): Completable = RxRealm.doTransactional {
        if (debt.id == null) {
            debt.id = UUID.randomUUID().toString()
        }
        val realmHolder = it.where(Holder::class.java).equalTo(Holder.NAME, debt.holder.name).findFirst()
        if (realmHolder != null) {
            debt.holder = realmHolder
        } else {
            debt.holder.id = UUID.randomUUID().toString()
        }
        if (debt.createdAt == 0L) {
            debt.createdAt = System.currentTimeMillis()
        }
        it.copyToRealmOrUpdate(debt)
    }

    fun getAll(): Flowable<List<Debt>> = RxRealm.listenList {
        it.where(Debt::class.java).findAllSorted(Debt.CREATED_AT)
    }

    fun getAllByHolder(holderId: String): Flowable<List<Debt>> = RxRealm.listenList {
        it.where(Debt::class.java)
                .equalTo(Debt.HOLDER_ID, holderId)
                .findAllSorted(Debt.CREATED_AT)
    }

    fun remove(debt: Debt): Completable = RxRealm.doTransactional {
        it.where(Debt::class.java).equalTo(Debt.ID, debt.id).findAll().deleteAllFromRealm()
    }
}
