package com.popalay.cardme.presentation.base.navigation

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import com.popalay.cardme.R
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.commands.ForwardForResult
import com.popalay.cardme.presentation.base.navigation.commands.ForwardToUrl
import com.popalay.cardme.presentation.base.navigation.commands.ForwardWithTransition
import com.popalay.cardme.presentation.screens.*
import com.popalay.cardme.presentation.screens.addcard.AddCardActivity
import com.popalay.cardme.presentation.screens.adddebt.AddDebtActivity
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsActivity
import com.popalay.cardme.presentation.screens.home.HomeActivity
import com.popalay.cardme.presentation.screens.settings.SettingsActivity
import com.popalay.cardme.utils.BrowserUtils
import com.popalay.cardme.utils.extensions.currentFragment
import com.popalay.cardme.utils.transitions.FabTransform
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

open class CustomNavigator(
        val activity: BaseActivity,
        val containerId: Int = 0
) : SupportAppNavigator(activity, containerId) {

    val fragmentManager: FragmentManager = activity.supportFragmentManager

    override fun applyCommand(command: Command?) {
        if (command is ForwardToUrl) {
            BrowserUtils.openLink(activity, command.url)
        } else if (command is ForwardForResult) {
            val activityIntent = createActivityIntent(command.screenKey, command.transitionData)

            // Start activity
            if (activityIntent != null) {
                val currentFragment = fragmentManager.currentFragment()
                if (currentFragment != null) {
                    currentFragment.startActivityForResult(activityIntent, command.requestCode)
                } else {
                    activity.startActivityForResult(activityIntent, command.requestCode)
                }
            }
        } else if (command is ForwardWithTransition) {
            val activityIntent = createActivityIntent(command.screenKey, command.transitionData)
            // Start activity
            if (activityIntent != null) {
                val currentFragment = fragmentManager.currentFragment()
                if (currentFragment != null) {
                    currentFragment.startActivity(activityIntent, command.transition)
                } else {
                    activity.startActivity(activityIntent, command.transition)
                }
            }
        }

        super.applyCommand(command)
    }

    override fun createFragment(screenKey: String, data: Any?): Fragment? = null

    override fun createActivityIntent(screenKey: String, data: Any?) = when (screenKey) {
        SCREEN_HOME -> HomeActivity.getIntent(activity)
        SCREEN_HOLDER_DETAILS -> HolderDetailsActivity.getIntent(activity, data as String)
        SCREEN_ADD_CARD -> AddCardActivity.getIntent(activity, data as CreditCard)
        SCREEN_SCAN_CARD -> Intent(activity, CardIOActivity::class.java)
        SCREEN_SETTINGS -> SettingsActivity.getIntent(activity)
        SCREEN_ADD_DEBT -> AddDebtActivity.getIntent(activity).apply {
            FabTransform.addExtras(this, ContextCompat.getColor(activity, R.color.accent), R.drawable.ic_write)
        }
        else -> null
    }

    override fun setupFragmentTransactionAnimation(command: Command?,
                                                   currentFragment: Fragment?,
                                                   nextFragment: Fragment?,
                                                   fragmentTransaction: FragmentTransaction?) {
        fragmentTransaction?.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
    }
}