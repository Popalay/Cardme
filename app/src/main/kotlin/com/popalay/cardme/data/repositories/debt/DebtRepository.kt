package com.popalay.cardme.data.repositories.debt

import com.github.popalay.rxrealm.RxRealm
import com.popalay.cardme.data.models.Debt
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
        if (debt.createdAt == 0L) {
            debt.createdAt = System.currentTimeMillis()
        }
        it.insertOrUpdate(debt.holder)
        it.insertOrUpdate(debt)
    }.toSingleDefault(debt)

    fun getAll(): Flowable<List<Debt>> = RxRealm.listenList {
        it.where(Debt::class.java)
                .equalTo(Debt.IS_TRASH, false)
                .findAllSorted(Debt.CREATED_AT)
    }

    fun getAllByHolder(holderName: String): Flowable<List<Debt>> = RxRealm.listenList {
        it.where(Debt::class.java)
                .equalTo(Debt.HOLDER_NAME, holderName)
                .equalTo(Debt.IS_TRASH, false)
                .findAllSorted(Debt.CREATED_AT)
    }

    fun markAsTrash(debt: Debt): Completable = RxRealm.doTransactional {
        it.where(Debt::class.java).equalTo(Debt.ID, debt.id).findFirst().isTrash = true
    }

    fun restore(debt: Debt): Completable = RxRealm.doTransactional {
        it.where(Debt::class.java).equalTo(Debt.ID, debt.id).findFirst().isTrash = false
    }

    fun removeTrashed(): Completable = RxRealm.doTransactional {
        it.where(Debt::class.java).equalTo(Debt.IS_TRASH, true).findAll().deleteAllFromRealm()
    }
}
