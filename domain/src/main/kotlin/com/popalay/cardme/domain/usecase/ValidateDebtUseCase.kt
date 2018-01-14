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
import io.reactivex.Single
import io.reactivex.rxkotlin.cast
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidateDebtUseCase @Inject constructor(
) : UseCase<ValidateDebtAction> {

    override fun apply(upstream: Observable<ValidateDebtAction>): ObservableSource<Result> =
        upstream.switchMap { action ->
            Single.fromCallable { action.debt.holderName.isNotBlank() && action.debt.message.isNotBlank() }
                .toObservable()
                .map(ValidateDebtResult::Success)
                .cast<ValidateDebtResult>()
                .onErrorReturn(ValidateDebtResult::Failure)
                .startWith(ValidateDebtResult.Idle(action.debt))
                .subscribeOn(Schedulers.io())
        }
}

data class ValidateDebtAction(val debt: Debt) : Action

sealed class ValidateDebtResult : Result {
    data class Success(val valid: Boolean) : ValidateDebtResult()
    data class Failure(val throwable: Throwable) : ValidateDebtResult()
    data class Idle(val debt: Debt) : ValidateDebtResult()
}
