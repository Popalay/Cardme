package com.popalay.cardme.presentation.base.navigation

import com.popalay.cardme.business.exception.AppException
import ru.terrakok.cicerone.Router

class CustomRouter : Router() {

    fun showError(exception: AppException) {
        this.executeCommand(Error(exception))
    }

}