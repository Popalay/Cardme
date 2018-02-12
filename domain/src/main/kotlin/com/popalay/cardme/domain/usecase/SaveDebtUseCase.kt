/*
 * Created by popalay on 03.01.18 23:19
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 03.01.18 22:52
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Debt
import com.popalay.cardme.domain.repository.DebtRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SaveDebtUseCase @Inject constructor(
    private val debtRepository: DebtRepository
) : UseCase<SaveDebtUseCase.Action, SaveDebtUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<SaveDebtUseCase.Result> =
        upstream.switchMap {
            debtRepository.save(it.card)
                .toSingleDefault(Result.Success)
                .cast(Result::class.java)
                .onErrorReturn(Result::Failure)
                .toObservable()
                .startWith(Result.Idle)
                .subscribeOn(Schedulers.io())
        }

    data class Action(val card: Debt) : UseCase.Action

    sealed class Result : UseCase.Result {
        object Success : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}
