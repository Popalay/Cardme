package com.popalay.cardme.base.navigation

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.popalay.cardme.R
import com.popalay.cardme.base.BaseActivity
import com.popalay.cardme.base.navigation.commands.ForwardForResult
import com.popalay.cardme.base.navigation.commands.ForwardToUrl
import com.popalay.cardme.utils.extensions.currentFragment
import com.popalay.cardme.utils.extensions.openLink
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

open class CustomNavigator(
	private val activity: BaseActivity,
	containerId: Int = 0
) : SupportAppNavigator(activity, containerId) {

	override fun applyCommand(command: Command?) {
		when (command) {
			is ForwardToUrl -> activity.openLink(command.url)
			is ForwardForResult -> createActivityIntent(command.screenKey, command.transitionData)?.let {
				activity.currentFragment()?.startActivityForResult(it, command.requestCode)
						?: activity.startActivityForResult(it, command.requestCode)
			}
			else -> super.applyCommand(command)
		}
	}

	override fun createFragment(screenKey: String, data: Any?): Fragment? = null

	override fun createActivityIntent(screenKey: String, data: Any?): Intent? = null

	override fun setupFragmentTransactionAnimation(
		command: Command?,
		currentFragment: Fragment?,
		nextFragment: Fragment?,
		fragmentTransaction: FragmentTransaction?
	) {
		fragmentTransaction?.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
	}
}