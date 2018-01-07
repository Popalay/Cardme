package com.popalay.cardme.screens.carddetails

import com.popalay.cardme.base.navigation.CustomNavigator
import javax.inject.Inject

class CardDetailsNavigator @Inject constructor(
        private val activity: CardDetailsActivity
) : CustomNavigator(activity) {

    override fun exit() {
        activity.runOnUiThread { activity.exitWithAnimation() }
    }
}