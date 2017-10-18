package com.popalay.cardme.domain.interactor

import com.popalay.cardme.domain.model.Debt
import com.popalay.cardme.domain.model.Holder
import com.popalay.cardme.domain.repository.DebtRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtsInteractor @Inject constructor(
        private val debtRepository: DebtRepository,
        private val holderInteractor: HolderInteractor
) {

    fun save(debt: Debt): Completable {
        return holderInteractor.save(Holder(name = debt.holderName))
                .andThen(debtRepository.save(debt))
                .subscribeOn(Schedulers.io())
    }

    fun getAll(): Flowable<List<Debt>> = debtRepository.getAllNotTrashed()
            .subscribeOn(Schedulers.io())

    fun markAsTrash(debt: Debt): Completable = debtRepository.markAsTrash(debt)
            .subscribeOn(Schedulers.io())

    fun restore(debt: Debt): Completable = debtRepository.restore(debt)
            .subscribeOn(Schedulers.io())
}
