/*
 * Created by popalay on 03.01.18 23:13
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 03.01.18 12:18
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Debt
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidateDebtUseCase @Inject constructor(
) : UseCase<ValidateDebtUseCase.Action, ValidateDebtUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        Observable.fromCallable {
            if (action.debt.holderName.isNotBlank() && action.debt.message.isNotBlank()) {
                Result.Valid(action.debt)
            } else {
                // TODO: Replace IllegalStateException with specific ValidationException
                Result.Invalid(action.debt, IllegalStateException("Debt's holder name and message cannot be blank"))
            }
        }
            .onErrorReturn { Result.Invalid(action.debt, it) }
            .subscribeOn(Schedulers.io())
    }

    data class Action(val debt: Debt) : UseCase.Action

    sealed class Result(debt: Debt) : UseCase.Result {
        data class Valid(val debt: Debt) : Result(debt)
        data class Invalid(val debt: Debt, val throwable: Throwable) : Result(debt)
    }
}