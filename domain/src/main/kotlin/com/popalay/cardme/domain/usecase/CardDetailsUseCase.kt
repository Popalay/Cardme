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
import io.reactivex.rxkotlin.cast
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CardDetailsUseCase @Inject constructor(
        private val cardRepository: CardRepository
) : UseCase<GetCardDetailsAction> {

    override fun apply(upstream: Observable<GetCardDetailsAction>): ObservableSource<Result> =
            upstream.switchMap {
                cardRepository.get(it.number)
                        .toObservable()
                        .map(CardDetailsResult::Success)
                        .cast<CardDetailsResult>()
                        .onErrorReturn(CardDetailsResult::Failure)
                        .subscribeOn(Schedulers.io())
            }
}

data class GetCardDetailsAction(val number: String) : Action

sealed class CardDetailsResult : Result {
    data class Success(val card: Card) : CardDetailsResult()
    data class Failure(val throwable: Throwable) : CardDetailsResult()
}