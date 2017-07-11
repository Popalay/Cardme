package com.popalay.cardme.presentation.base.navigation

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.Toast
import com.popalay.cardme.R
import com.popalay.cardme.business.exception.AppException
import com.popalay.cardme.presentation.base.BaseActivity
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

abstract class CustomNavigator(
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
                activity.startActivityForResult(activityIntent, command.requestCode)
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

    override fun createActivityIntent(screenKey: String, data: Any?): Intent? = null

    open fun showError(exception: AppException) {
        Toast.makeText(activity, exception.messageRes, Toast.LENGTH_LONG).show()
    }
}