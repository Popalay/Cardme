package com.popalay.cardme.presentation.base.navigation

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.Toast
import com.popalay.cardme.R
import com.popalay.cardme.business.exception.AppException
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.screens.SCREEN_ADD_CARD
import com.popalay.cardme.presentation.screens.SCREEN_HOLDER_DETAILS
import com.popalay.cardme.presentation.screens.SCREEN_SCAN_CARD
import com.popalay.cardme.presentation.screens.SCREEN_SETTINGS
import com.popalay.cardme.presentation.screens.addcard.AddCardActivity
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsActivity
import com.popalay.cardme.presentation.screens.settings.SettingsActivity
import com.popalay.cardme.utils.extensions.currentFragment
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

open class CustomNavigator(
        val activity: BaseActivity,
        val containerId: Int = 0
) : SupportAppNavigator(activity, containerId) {

    val fragmentManager: FragmentManager = activity.supportFragmentManager

    override fun applyCommand(command: Command?) {
        if (command is Error) {
            showError(command.exception)
            return
        }
        if (command is ForwardForResult) {
            val activityIntent = createActivityIntent(command.screenKey, command.transitionData)

            // Start activity
            if (activityIntent != null) {
                val currentFragment = fragmentManager.currentFragment()
                if (currentFragment != null) {
                    currentFragment.startActivityForResult(activityIntent, command.requestCode)
                } else {
                    activity.startActivityForResult(activityIntent, command.requestCode)
                }
                return
            }
        }

        if (command is Forward) {
            val forward = command
            val activityIntent = createActivityIntent(forward.screenKey, forward.transitionData)

            // Start activity
            if (activityIntent != null) {
                activity.startActivity(activityIntent)
                return
            }
        } else if (command is Replace) {
            val replace = command
            val activityIntent = createActivityIntent(replace.screenKey, replace.transitionData)

            // Replace activity
            if (activityIntent != null) {
                activity.startActivity(activityIntent)
                activity.finish()
                return
            }
        }

        if (command is Forward) {
            val forward = command
            val fragment = createFragment(forward.screenKey, forward.transitionData)
            if (fragment == null) {
                unknownScreen(command)
                return
            }
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(containerId, fragment)
                    .addToBackStack(forward.screenKey)
                    .commit()
            return
        } else if (command is Back) {
            if (fragmentManager.backStackEntryCount > 0) {
                fragmentManager.popBackStackImmediate()
                return
            } else {
                exit()
                return
            }
        } else if (command is Replace) {
            val replace = command
            val fragment = createFragment(replace.screenKey, replace.transitionData)
            if (fragment == null) {
                unknownScreen(command)
                return
            }
            if (fragmentManager.backStackEntryCount > 0) {
                fragmentManager.popBackStackImmediate()
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(containerId, fragment)
                        .addToBackStack(replace.screenKey)
                        .commit()
                return
            } else {
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(containerId, fragment)
                        .commit()
                return
            }
        }

        super.applyCommand(command)
    }

    override fun createFragment(screenKey: String, data: Any?): Fragment? = null

    override fun createActivityIntent(screenKey: String, data: Any?) = when (screenKey) {
        SCREEN_HOLDER_DETAILS -> HolderDetailsActivity.getIntent(activity, data as String)
        SCREEN_ADD_CARD -> AddCardActivity.getIntent(activity, data as CreditCard)
        SCREEN_SCAN_CARD -> Intent(activity, CardIOActivity::class.java)
        SCREEN_SETTINGS -> SettingsActivity.getIntent(activity)
        else -> null
    }

    open fun showError(exception: AppException) {
        Toast.makeText(activity, exception.messageRes, Toast.LENGTH_LONG).show()
    }
}