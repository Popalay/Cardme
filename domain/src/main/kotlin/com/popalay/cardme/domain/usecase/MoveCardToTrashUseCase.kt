/*
 * Created by popalay on 04.01.18 23:21
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 04.01.18 22:16
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.repository.CardRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.rxkotlin.cast
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MoveCardToTrashUseCase @Inject constructor(
    private val cardRepository: CardRepository
) : UseCase<MoveCardToTrashAction> {

    override fun apply(upstream: Observable<MoveCardToTrashAction>): ObservableSource<Result> =
        upstream.switchMap {
            cardRepository.markAsTrash(it.card)
                .toSingleDefault(MoveCardToTrashResult.Success)
                .toObservable()
                .cast<MoveCardToTrashResult>()
                .onErrorReturn(MoveCardToTrashResult::Failure)
                .subscribeOn(Schedulers.io())
        }
}

data class MoveCardToTrashAction(val card: Card) : Action

sealed class MoveCardToTrashResult : Result {
    object Success : MoveCardToTrashResult()
    data class Failure(val throwable: Throwable) : MoveCardToTrashResult()
}