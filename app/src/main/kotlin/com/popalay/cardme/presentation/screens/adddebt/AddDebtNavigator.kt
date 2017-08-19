package com.popalay.cardme.presentation.screens.adddebt

import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import javax.inject.Inject

class AddDebtNavigator @Inject constructor(
        private val activity: AddDebtActivity
) : CustomNavigator(activity) {

    override fun exit() = activity.exitWithAnimation()

}
