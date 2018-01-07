package com.popalay.cardme.screens.home

import android.content.Intent
import android.os.Bundle
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.screens.SCREEN_ADD_CARD
import com.popalay.cardme.screens.SCREEN_ADD_DEBT
import com.popalay.cardme.screens.SCREEN_CARD_DETAILS
import com.popalay.cardme.screens.SCREEN_HOLDER_DETAILS
import com.popalay.cardme.screens.SCREEN_HOME
import com.popalay.cardme.screens.SCREEN_SCAN_CARD
import com.popalay.cardme.screens.SCREEN_SETTINGS
import com.popalay.cardme.screens.SCREEN_TRASH
import com.popalay.cardme.screens.adddebt.AddDebtActivity
import com.popalay.cardme.screens.carddetails.CardDetailsActivity
import com.popalay.cardme.screens.cards.CardsFragment
import com.popalay.cardme.screens.debts.DebtsFragment
import com.popalay.cardme.screens.holderdetails.HolderDetailsActivity
import com.popalay.cardme.screens.settings.SettingsActivity
import com.popalay.cardme.screens.trash.TrashActivity
import com.popalay.cardme.utils.extensions.findFragmentByType
import io.card.payment.CardIOActivity
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import javax.inject.Inject

class HomeNavigator @Inject constructor(
        private val activity: HomeActivity
) : CustomNavigator(activity) {

    @Suppress("UNCHECKED_CAST")
    override fun createActivityIntent(screenKey: String, data: Any?) = when (screenKey) {
        SCREEN_HOME -> HomeActivity.getIntent(activity)
        SCREEN_HOLDER_DETAILS -> HolderDetailsActivity.getIntent(activity, data as String)
        SCREEN_ADD_CARD -> CardDetailsActivity.getIntent(activity, data as String)
        SCREEN_SCAN_CARD -> Intent(activity, CardIOActivity::class.java)
        SCREEN_SETTINGS -> SettingsActivity.getIntent(activity)
        SCREEN_ADD_DEBT -> AddDebtActivity.getIntent(activity)
        SCREEN_TRASH -> TrashActivity.getIntent(activity)
        SCREEN_CARD_DETAILS -> CardDetailsActivity.getIntent(activity, data as String)
        else -> null
    }

    override fun createStartActivityOptions(command: Command?, activityIntent: Intent): Bundle? {
        if (command is Forward) {
            return when (command.screenKey) {
                SCREEN_ADD_DEBT -> activity.findFragmentByType<DebtsFragment>()?.createAddDebtTransition(activityIntent)
                SCREEN_CARD_DETAILS -> activity.findFragmentByType<CardsFragment>()?.createCardDetailsTransition(activityIntent)
                else -> super.createStartActivityOptions(command, activityIntent)
            }
        }
        return super.createStartActivityOptions(command, activityIntent)
    }
}