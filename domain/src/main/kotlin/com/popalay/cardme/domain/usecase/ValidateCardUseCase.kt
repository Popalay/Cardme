/*
 * Created by popalay on 26.12.17 23:40
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 16.12.17 20:28
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.usecase.ValidateCardAction.ValidateCardError
import com.popalay.cardme.domain.usecase.ValidateCardAction.ValidateCardResult
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidateCardUseCase @Inject constructor(
) {

    fun execute(card: Card?, holderName: String): Single<ValidateCardAction> = Single.fromCallable {
        holderName.isNotBlank()
    }
            .map { ValidateCardResult(it) as ValidateCardAction }
            .onErrorReturn { ValidateCardError }
}

sealed class ValidateCardAction : Action {
    data class ValidateCardResult(val valid: Boolean) : ValidateCardAction()
    object ValidateCardError : ValidateCardAction()
}
