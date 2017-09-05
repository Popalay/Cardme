package com.popalay.cardme.data.repository

import com.github.popalay.rxrealm.RxRealm
import com.popalay.cardme.domain.model.Debt
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataDebtRepository @Inject constructor() {

    fun getAll(): Flowable<List<Debt>> = RxRealm.listenList {
        it.where(Debt::class.java)
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
