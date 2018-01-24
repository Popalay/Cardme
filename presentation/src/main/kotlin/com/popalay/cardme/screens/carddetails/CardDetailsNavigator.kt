package com.popalay.cardme.screens.carddetails

import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.screens.SCREEN_ADD_CARD
import com.popalay.cardme.screens.addcard.AddCardActivity
import javax.inject.Inject

class CardDetailsNavigator @Inject constructor(
	private val activity: CardDetailsActivity
) : CustomNavigator(activity) {

	override fun createActivityIntent(screenKey: String, data: Any?) = when (screenKey) {
		SCREEN_ADD_CARD -> AddCardActivity.getIntent(activity, data as String)
		else -> null
	}

	override fun exit() {
		activity.runOnUiThread { activity.exitWithAnimation() }
	}
}