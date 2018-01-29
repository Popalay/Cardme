/*
 * Created by popalay on 04.01.18 23:00
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 03.01.18 23:54
 */

package com.popalay.cardme.screens.carddetails

import com.popalay.cardme.base.mvi.ViewState
import com.popalay.cardme.domain.model.Card

data class CardDetailsViewState(
        val card: Card,
        val showBackground: Boolean,
        val showShareDialog: Boolean,
        val nfcEnabled: Boolean,
        val animateButtons: Boolean,
        val error: Throwable?
) : ViewState {
    companion object {
        fun idle() = CardDetailsViewState(
                card = Card(),
                showBackground = false,
                showShareDialog = false,
                nfcEnabled = false,
                animateButtons = false,
                error = null
        )
    }
}