package com.popalay.cardme.presentation.base.navigation

import ru.terrakok.cicerone.commands.Command

class ForwardForResult(
        val screenKey: String,
        val transitionData: Any? = null,
        val requestCode: Int
) : Command