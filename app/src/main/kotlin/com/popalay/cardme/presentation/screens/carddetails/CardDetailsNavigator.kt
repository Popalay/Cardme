package com.popalay.cardme.presentation.screens.home

import com.popalay.cardme.data.models.Card
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.presentation.screens.SCREEN_ADD_CARD
import com.popalay.cardme.presentation.screens.addcard.AddCardActivity
import com.popalay.cardme.presentation.screens.carddetails.CardDetailsActivity
import javax.inject.Inject

class CardDetailsNavigator @Inject constructor(
        private val activity: CardDetailsActivity
) : CustomNavigator(activity) {

    @Suppress("UNCHECKED_CAST")
    override fun createActivityIntent(screenKey: String, data: Any?) = when (screenKey) {
        SCREEN_ADD_CARD -> AddCardActivity.getIntent(activity, data as Card)
        else -> null
    }

    override fun exit() = activity.exitWithAnimation()
}