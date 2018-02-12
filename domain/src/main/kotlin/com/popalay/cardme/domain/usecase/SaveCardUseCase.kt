/*
 * Created by popalay on 30.12.17 22:24
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 22:23
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.repository.CardRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SaveCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) : UseCase<SaveCardUseCase.Action, SaveCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap {
        cardRepository.save(it.card.copy(isPending = false))
            .toSingleDefault(Result.Success)
            .toObservable()
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    sealed class Result : UseCase.Result {
        object Idle : Result()
        object Success : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    data class Action(val card: Card) : UseCase.Action
}