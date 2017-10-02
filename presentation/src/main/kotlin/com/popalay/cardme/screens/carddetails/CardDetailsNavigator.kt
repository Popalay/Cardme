package com.popalay.cardme.screens.home

import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.screens.SCREEN_ADD_CARD
import com.popalay.cardme.screens.addcard.AddCardActivity
import com.popalay.cardme.screens.carddetails.CardDetailsActivity
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