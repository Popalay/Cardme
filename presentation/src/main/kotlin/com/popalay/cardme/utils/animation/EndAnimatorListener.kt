package com.popalay.cardme.utils.animation

import android.animation.Animator

interface EndAnimatorListener : Animator.AnimatorListener {

    override fun onAnimationStart(animation: Animator) = Unit

    override fun onAnimationCancel(animation: Animator) = Unit

    override fun onAnimationRepeat(animation: Animator) = Unit
}
