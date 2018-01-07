/*
 * Created by popalay on 07.01.18 23:08
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 04.01.18 22:16
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Card
import io.reactivex.Observable
import io.reactivex.ObservableSource
import javax.inject.Inject

class EditCardUseCase @Inject constructor(
) : UseCase<EditCardAction> {

    override fun apply(upstream: Observable<EditCardAction>): ObservableSource<Result> =
            upstream.switchMap {
                Observable.just(EditCardResult.Success)
                        .cast(EditCardResult::class.java)
            }
}

data class EditCardAction(val card: Card) : Action

sealed class EditCardResult : Result {
    object Success : EditCardResult()
}