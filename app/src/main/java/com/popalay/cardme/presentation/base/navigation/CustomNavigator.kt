package com.popalay.cardme.presentation.base.navigation

import android.content.Intent
import android.support.v4.app.Fragment
import android.widget.Toast
import com.popalay.cardme.business.exception.AppException
import com.popalay.cardme.presentation.base.BaseActivity
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

abstract class CustomNavigator(
        val activity: BaseActivity,
        container: Int = 0
) : SupportAppNavigator(activity, container) {

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

        super.applyCommand(command)
    }

    override fun createFragment(screenKey: String, data: Any?): Fragment? {
        return null
    }

    override fun createActivityIntent(screenKey: String, data: Any?): Intent? {
        return null
    }

    open fun showError(exception: AppException) {
        Toast.makeText(activity, exception.messageRes, Toast.LENGTH_LONG).show()
    }
}