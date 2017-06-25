package com.popalay.cardme.utils.animation;

import android.animation.Animator;

public abstract class EndAnimatorListener implements Animator.AnimatorListener {

    @Override public void onAnimationStart(Animator animation) {}

    @Override public void onAnimationCancel(Animator animation) {}

    @Override public void onAnimationRepeat(Animator animation) {}
}
