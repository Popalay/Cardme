/*
 * Created by popalay on 26.12.17 22:48
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 16.12.17 20:28
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.repository.CardRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CardDetailsUseCase @Inject constructor(
    private val cardRepository: CardRepository
) : UseCase<CardDetailsUseCase.Action, CardDetailsUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap {
        cardRepository.get(it.number)
            .toObservable()
            .map(Result::Success)
            .cast(Result::class.java)
            .startWith(Result.Idle)
            .onErrorReturn(Result::Failure)
            .subscribeOn(Schedulers.io())
    }

    data class Action(val number: String) : UseCase.Action

    sealed class Result : UseCase.Result {
        object Idle : Result()
        data class Success(val card: Card) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}