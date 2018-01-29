package com.popalay.cardme.screens.holderdetails

import android.content.Intent
import android.os.Bundle
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.screens.SCREEN_CARD_DETAILS
import com.popalay.cardme.screens.carddetails.CardDetailsActivity
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import javax.inject.Inject

class HolderDetailsNavigator @Inject constructor(
	private val activity: HolderDetailsActivity
) : CustomNavigator(activity) {

	override fun createActivityIntent(screenKey: String, data: Any?) = when (screenKey) {
		SCREEN_CARD_DETAILS -> CardDetailsActivity.getIntent(activity, data as String)
		else -> null
	}

	override fun createStartActivityOptions(command: Command?, activityIntent: Intent): Bundle? {
		if (command is Forward && command.screenKey == SCREEN_CARD_DETAILS) {
			return activity.createCardDetailsTransition(activityIntent)
		}
		return super.createStartActivityOptions(command, activityIntent)
	}
}