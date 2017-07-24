package com.popalay.cardme.business.debts

import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.data.repositories.debt.DebtRepository
import com.popalay.cardme.data.repositories.holder.HolderRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtsInteractor @Inject constructor(
        private val debtRepository: DebtRepository,
        private val holderRepository: HolderRepository
) {

    fun save(debt: Debt): Completable = debtRepository.save(debt)
            .flatMapCompletable { holderRepository.updateCounts(it.holder) }
            .subscribeOn(Schedulers.io())

    fun getDebts(): Flowable<List<Debt>> = debtRepository.getAll()
            .subscribeOn(Schedulers.io())

    fun getDebtsByHolder(holderId: String): Flowable<List<Debt>> = debtRepository.getAllByHolder(holderId)
            .subscribeOn(Schedulers.io())

    fun remove(debt: Debt): Completable = debtRepository.remove(debt)
            .andThen(holderRepository.updateCounts(debt.holder))
            .subscribeOn(Schedulers.io())

    fun restore(debt: Debt): Completable = debtRepository.restore(debt)
            .andThen(holderRepository.updateCounts(debt.holder))
            .subscribeOn(Schedulers.io())
}
