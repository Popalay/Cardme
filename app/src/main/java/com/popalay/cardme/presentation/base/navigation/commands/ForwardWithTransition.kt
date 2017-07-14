package com.popalay.cardme.presentation.base.navigation.commands

import android.os.Bundle
import ru.terrakok.cicerone.commands.Command

class ForwardWithTransition(
        val screenKey: String,
        val transitionData: Any? = null,
        val transition: Bundle
) : Command