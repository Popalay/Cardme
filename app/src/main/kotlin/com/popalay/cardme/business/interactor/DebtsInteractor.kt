package com.popalay.cardme.business.interactor

import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.data.repositories.DebtRepository
import com.popalay.cardme.data.repositories.HolderRepository
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

    fun getAll(): Flowable<List<Debt>> = debtRepository.getAll()
            .subscribeOn(Schedulers.io())

    fun markAsTrash(debt: Debt): Completable = debtRepository.markAsTrash(debt)
            .andThen(holderRepository.removeDebt(debt))
            .subscribeOn(Schedulers.io())

    fun restore(debt: Debt): Completable = holderRepository.addDebt(debt.holder.name, debt)
            .andThen(debtRepository.restore(debt))
            .subscribeOn(Schedulers.io())
}
