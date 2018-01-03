/*
 * Created by popalay on 30.12.17 22:24
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 22:23
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.model.Holder
import com.popalay.cardme.domain.repository.CardRepository
import com.popalay.cardme.domain.repository.HolderRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SaveCardUseCase @Inject constructor(
        private val cardRepository: CardRepository,
        private val holderRepository: HolderRepository
) : UseCase<SaveCardAction> {

    override fun apply(upstream: Observable<SaveCardAction>): ObservableSource<Result> =
            upstream.switchMap {
                holderRepository.save(Holder(name = it.card.holderName))
                        .andThen(cardRepository.save(it.card))
                        .toSingleDefault(SaveCardResult.Success)
                        .cast(SaveCardResult::class.java)
                        .onErrorReturn(SaveCardResult::Failure)
                        .toObservable()
                        .subscribeOn(Schedulers.io())
            }
}

data class SaveCardAction(val card: Card) : Action

sealed class SaveCardResult : Result {
    object Success : SaveCardResult()
    data class Failure(val throwable: Throwable) : SaveCardResult()
}