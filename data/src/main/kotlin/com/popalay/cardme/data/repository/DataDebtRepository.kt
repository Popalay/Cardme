package com.popalay.cardme.data.repository

import com.github.popalay.rxrealm.RxRealm
import com.popalay.cardme.data.model.DataDebt
import com.popalay.cardme.domain.model.Debt
import com.popalay.cardme.domain.repository.DebtRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataDebtRepository @Inject constructor(
) : DebtRepository {

    override fun getAll(): Flowable<List<Debt>> = RxRealm.listenList {
        it.where(DataDebt::class.java)
                .equalTo(DataDebt.IS_TRASH, false)
                .findAllSorted(DataDebt.CREATED_AT)
    }

    override fun markAsTrash(debt: Debt): Completable = RxRealm.doTransactional {
        it.where(DataDebt::class.java).equalTo(DataDebt.ID, debt.id).findFirst().isTrash = true
    }

    override fun restore(debt: Debt): Completable = RxRealm.doTransactional {
        it.where(DataDebt::class.java).equalTo(DataDebt.ID, debt.id).findFirst().isTrash = false
    }

    override fun removeTrashed(): Completable = RxRealm.doTransactional {
        it.where(DataDebt::class.java).equalTo(DataDebt.IS_TRASH, true).findAll().deleteAllFromRealm()
    }
}
