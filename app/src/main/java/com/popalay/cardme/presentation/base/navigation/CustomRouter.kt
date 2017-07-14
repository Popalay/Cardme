package com.popalay.cardme.presentation.base.navigation

import android.os.Bundle
import com.popalay.cardme.presentation.base.navigation.commands.ForwardForResult
import com.popalay.cardme.presentation.base.navigation.commands.ForwardToUrl
import com.popalay.cardme.presentation.base.navigation.commands.ForwardWithTransition
import ru.terrakok.cicerone.Router

class CustomRouter : Router() {

    fun navigateToForResult(screenKey: String, data: Any? = null, requestCode: Int) {
        executeCommand(ForwardForResult(screenKey, data, requestCode))
    }

    fun navigateToWithTransition(screenKey: String, data: Any? = null, transition: Bundle) {
        executeCommand(ForwardWithTransition(screenKey, data, transition))
    }

    fun navigateToUrl(url: String) {
        executeCommand(ForwardToUrl(url))
    }

}