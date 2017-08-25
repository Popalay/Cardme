package com.popalay.cardme.presentation.base.navigation

import com.popalay.cardme.presentation.base.navigation.commands.ForwardForResult
import com.popalay.cardme.presentation.base.navigation.commands.ForwardToUrl
import ru.terrakok.cicerone.Router

class CustomRouter : Router() {

    fun navigateToForResult(screenKey: String, requestCode: Int,  data: Any? = null) {
        executeCommand(ForwardForResult(screenKey, data, requestCode))
    }

    fun navigateToUrl(url: String) {
        executeCommand(ForwardToUrl(url))
    }

}