package com.popalay.cardme.presentation.base.navigation

import com.popalay.cardme.business.exception.AppException

import ru.terrakok.cicerone.commands.Command

/**
 * Shows app error.
 */
class Error(val exception: AppException) : Command