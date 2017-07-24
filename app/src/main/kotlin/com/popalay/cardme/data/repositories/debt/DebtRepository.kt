package com.popalay.cardme.data.repositories.debt

import com.github.popalay.rxrealm.RxRealm
import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.data.models.Holder
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtRepository @Inject internal constructor() {

    fun save(debt: Debt): Single<Debt> = RxRealm.doTransactional {
        if (debt.id == null) {
            debt.id = UUID.randomUUID().toString()
        }
        val realmHolder = it.where(Holder::class.java).equalTo(Holder.NAME, debt.holder.name).findFirst()
        if (realmHolder != null) {
            debt.holder = it.copyFromRealm(realmHolder)
        } else {
            debt.holder.id = UUID.randomUUID().toString()
        }
        if (debt.createdAt == 0L) {
            debt.createdAt = System.currentTimeMillis()
        }
        it.insertOrUpdate(debt)
    }.toSingleDefault(debt)

    fun getAll(): Flowable<List<Debt>> = RxRealm.listenList {
        it.where(Debt::class.java)
                .equalTo(Debt.IS_TRASH, false)
                .findAllSorted(Debt.CREATED_AT)
    }

    fun getAllByHolder(holderId: String): Flowable<List<Debt>> = RxRealm.listenList {
        it.where(Debt::class.java)
                .equalTo(Debt.HOLDER_ID, holderId)
                .equalTo(Debt.IS_TRASH, false)
                .findAllSorted(Debt.CREATED_AT)
    }

    fun markAsTrash(debt: Debt): Completable = RxRealm.doTransactional {
        it.where(Debt::class.java).equalTo(Debt.ID, debt.id).findFirst().isTrash = true
    }

    fun restore(debt: Debt): Completable = RxRealm.doTransactional {
        it.where(Debt::class.java).equalTo(Debt.ID, debt.id).findFirst().isTrash = false
    }
}
