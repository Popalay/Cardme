/*
 * Created by popalay on 30.12.17 22:10
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 22:10
 */

package com.popalay.cardme.screens.addcard

import com.popalay.cardme.base.mvi.ViewState
import com.popalay.cardme.domain.model.Card

data class AddCardViewState(
    val card: Card,
    val showBackground: Boolean,
    val holderNames: List<String>,
    val canSave: Boolean,
    val error: Throwable?
) : ViewState {

    val shouldShowSuggestions = holderNames.size == 1 && holderNames.first() != card.holderName || holderNames.isNotEmpty()

    companion object {
        fun idle() = AddCardViewState(
            card = Card(),
            showBackground = false,
            holderNames = listOf(),
            canSave = false,
            error = null
        )
    }
}