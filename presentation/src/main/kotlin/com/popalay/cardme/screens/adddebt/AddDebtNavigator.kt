package com.popalay.cardme.screens.adddebt

import com.popalay.cardme.base.navigation.CustomNavigator
import javax.inject.Inject

class AddDebtNavigator @Inject constructor(
        private val activity: AddDebtActivity
) : CustomNavigator(activity) {

    override fun exit() = activity.exitWithAnimation()

}
