/*
 * Created by popalay on 26.12.17 23:40
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 16.12.17 20:28
 */

package com.popalay.cardme.domain.usecase

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidateCardUseCase @Inject constructor(
) : UseCase<ValidateCardAction> {

    override fun apply(upstream: Observable<ValidateCardAction>): ObservableSource<Result> =
            upstream.switchMap {
                Single.fromCallable { it.holderName.isNotBlank() }
                        .toObservable()
                        .map(ValidateCardResult::Success)
                        .cast(ValidateCardResult::class.java)
                        .onErrorReturn(ValidateCardResult::Failure)
                        .startWith(ValidateCardResult.Idle)
                        .subscribeOn(Schedulers.io())
            }
}

data class ValidateCardAction(val holderName: String) : Action

sealed class ValidateCardResult : Result {
    data class Success(val valid: Boolean) : ValidateCardResult()
    data class Failure(val throwable: Throwable) : ValidateCardResult()
    object Idle : ValidateCardResult()
}
