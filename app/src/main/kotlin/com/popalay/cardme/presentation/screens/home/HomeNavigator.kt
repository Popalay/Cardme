package com.popalay.cardme.presentation.screens.home

import android.content.Intent
import android.os.Bundle
import com.popalay.cardme.R
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.presentation.screens.*
import com.popalay.cardme.presentation.screens.addcard.AddCardActivity
import com.popalay.cardme.presentation.screens.adddebt.AddDebtActivity
import com.popalay.cardme.presentation.screens.carddetails.CardDetailsActivity
import com.popalay.cardme.presentation.screens.cards.CardsFragment
import com.popalay.cardme.presentation.screens.debts.DebtsFragment
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsActivity
import com.popalay.cardme.presentation.screens.holders.HoldersFragment
import com.popalay.cardme.presentation.screens.settings.SettingsActivity
import com.popalay.cardme.presentation.screens.trash.TrashActivity
import com.popalay.cardme.utils.extensions.findFragmentByType
import io.card.payment.CardIOActivity
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import javax.inject.Inject

class HomeNavigator @Inject constructor(
        private val activity: HomeActivity
) : CustomNavigator(activity, R.id.host) {

    override fun createFragment(screenKey: String, data: Any?) = when (screenKey) {
        SCREEN_CARDS -> {
            activity.selectTab(R.id.cards)
            CardsFragment.newInstance()
        }
        SCREEN_HOLDERS -> {
            activity.selectTab(R.id.holders)
            HoldersFragment.newInstance()
        }
        SCREEN_DEBTS -> {
            activity.selectTab(R.id.debts);
            DebtsFragment.newInstance()
        }
        else -> null
    }

    @Suppress("UNCHECKED_CAST")
    override fun createActivityIntent(screenKey: String, data: Any?) = when (screenKey) {
        SCREEN_HOME -> HomeActivity.getIntent(activity)
        SCREEN_HOLDER_DETAILS -> HolderDetailsActivity.getIntent(activity, data as String)
        SCREEN_ADD_CARD -> AddCardActivity.getIntent(activity, data as Card)
        SCREEN_SCAN_CARD -> Intent(activity, CardIOActivity::class.java)
        SCREEN_SETTINGS -> SettingsActivity.getIntent(activity)
        SCREEN_ADD_DEBT -> AddDebtActivity.getIntent(activity)
        SCREEN_TRASH -> TrashActivity.getIntent(activity)
        SCREEN_CARD_DETAILS -> CardDetailsActivity.getIntent(activity, data as String)
        else -> null
    }

    override fun setupActivityTransactionAnimation(command: Command, activityIntent: Intent): Bundle? {
        if (command is Forward && command.screenKey == SCREEN_ADD_DEBT) {
            return activity.findFragmentByType<DebtsFragment>()?.createAddDebtTransition(activityIntent)
        } else if (command is Forward && command.screenKey == SCREEN_CARD_DETAILS) {
            return activity.findFragmentByType<CardsFragment>()?.createCardDetailsTransition(activityIntent)
        }
        return super.setupActivityTransactionAnimation(command, activityIntent)
    }

}