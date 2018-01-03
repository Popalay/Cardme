/*
 * Created by popalay on 03.01.18 23:19
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 03.01.18 22:52
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Debt
import com.popalay.cardme.domain.model.Holder
import com.popalay.cardme.domain.repository.DebtRepository
import com.popalay.cardme.domain.repository.HolderRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SaveDebtUseCase @Inject constructor(
        private val debtRepository: DebtRepository,
        private val holderRepository: HolderRepository
) : UseCase<SaveDebtAction> {

    override fun apply(upstream: Observable<SaveDebtAction>): ObservableSource<Result> =
            upstream.switchMap {
                holderRepository.save(Holder(name = it.card.holderName))
                        .andThen(debtRepository.save(it.card))
                        .toSingleDefault(SaveDebtResult.Success)
                        .cast(SaveDebtResult::class.java)
                        .onErrorReturn(SaveDebtResult::Failure)
                        .toObservable()
                        .startWith(SaveDebtResult.Idle)
                        .subscribeOn(Schedulers.io())
            }
}

data class SaveDebtAction(val card: Debt) : Action

sealed class SaveDebtResult : Result {
    object Success : SaveDebtResult()
    object Idle : SaveDebtResult()
    data class Failure(val throwable: Throwable) : SaveDebtResult()
}