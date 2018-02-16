/*
 * Created by popalay on 26.12.17 23:40
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 16.12.17 20:28
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Card
import io.reactivex.Observable
import io.reactivex.ObservableSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidateCardUseCase @Inject constructor(
) : UseCase<ValidateCardUseCase.Action, ValidateCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> =
        upstream
            .distinctUntilChanged()
            .map { action ->
                if (action.card.holderName.isNotBlank()) {
                    Result.Valid(action.card)
                } else {
                    Result.Invalid(action.card)
                }
            }

    data class Action(val card: Card) : UseCase.Action

    sealed class Result(card: Card) : UseCase.Result {
        data class Valid(val card: Card) : Result(card)
        data class Invalid(val card: Card) : Result(card)
    }
}