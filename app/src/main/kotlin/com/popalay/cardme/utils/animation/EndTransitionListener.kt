package com.popalay.cardme.utils.animation

import android.transition.Transition

interface EndTransitionListener : Transition.TransitionListener {

    override fun onTransitionStart(transition: Transition) = Unit

    override fun onTransitionCancel(transition: Transition) = Unit

    override fun onTransitionPause(transition: Transition) = Unit

    override fun onTransitionResume(transition: Transition) = Unit
}
