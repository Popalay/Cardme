/*
 * Created by popalay on 30.12.17 22:10
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 22:10
 */

package com.popalay.cardme.screens.addcard

import com.popalay.cardme.base.mvi.Intent
import com.popalay.cardme.domain.model.Card

sealed class AddCardIntent : Intent {

    sealed class Initial : AddCardIntent() {
        data class GetCard(val number: String) : AddCardIntent.Initial()
        object GetShouldShowBackground : AddCardIntent.Initial()
    }

    data class CardNameChanged(val card: Card) : AddCardIntent()
    data class CardTitleChanged(val card: Card) : AddCardIntent()
    data class Accept(val card: Card) : AddCardIntent()
}