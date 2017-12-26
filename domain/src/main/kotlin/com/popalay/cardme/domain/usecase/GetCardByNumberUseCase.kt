/*
 * Created by popalay on 26.12.17 22:48
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 16.12.17 20:28
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.repository.CardRepository
import com.popalay.cardme.domain.usecase.GetCardByNumberAction.CardResultError
import com.popalay.cardme.domain.usecase.GetCardByNumberAction.CardResultProgress
import com.popalay.cardme.domain.usecase.GetCardByNumberAction.CardResultSuccess
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetCardByNumberUseCase @Inject constructor(
        private val cardRepository: CardRepository
) {

    fun execute(cardNumber: String): Flowable<GetCardByNumberAction> = cardRepository.get(cardNumber)
            .map { CardResultSuccess(it) as GetCardByNumberAction }
            .onErrorReturn { CardResultError(it) }
            .startWith(CardResultProgress)
            .subscribeOn(Schedulers.io())
}

interface Action

sealed class GetCardByNumberAction : Action {
    data class CardResultSuccess(val card: Card) : GetCardByNumberAction()
    data class CardResultError(val throwable: Throwable) : GetCardByNumberAction()
    object CardResultProgress : GetCardByNumberAction()
}