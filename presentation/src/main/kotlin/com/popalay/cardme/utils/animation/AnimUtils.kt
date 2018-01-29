package com.popalay.cardme.utils.animation

import android.animation.Animator
import android.animation.TimeInterpolator
import android.content.Context
import android.util.ArrayMap
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import java.util.ArrayList

object AnimUtils {

	private var fastOutSlowIn: Interpolator? = null
	private var fastOutLinearIn: Interpolator? = null
	private var linearOutSlowIn: Interpolator? = null

	fun getFastOutSlowInInterpolator(context: Context): Interpolator {
		if (fastOutSlowIn == null) {
			fastOutSlowIn = AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in)
		}
		return fastOutSlowIn!!
	}

	fun getFastOutLinearInInterpolator(context: Context): Interpolator {
		if (fastOutLinearIn == null) {
			fastOutLinearIn = AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_linear_in)
		}
		return fastOutLinearIn!!
	}

	fun getLinearOutSlowInInterpolator(context: Context): Interpolator {
		if (linearOutSlowIn == null) {
			linearOutSlowIn = AnimationUtils.loadInterpolator(context, android.R.interpolator.linear_out_slow_in)
		}
		return linearOutSlowIn!!
	}

	class NoPauseAnimator(private val animator: Animator) : Animator() {

		private val listeners = ArrayMap<Animator.AnimatorListener, Animator.AnimatorListener>()

		override fun addListener(listener: Animator.AnimatorListener) {
			val wrapper = AnimatorListenerWrapper(this, listener)
			if (!listeners.containsKey(listener)) {
				listeners[listener] = wrapper
				animator.addListener(wrapper)
			}
		}

		override fun start() = animator.start()

		override fun cancel() = animator.cancel()

		override fun end() = animator.end()

		override fun getDuration() = animator.duration

		override fun getInterpolator(): TimeInterpolator = animator.interpolator

		override fun setInterpolator(timeInterpolator: TimeInterpolator) {
			animator.interpolator = timeInterpolator
		}

		override fun getListeners() = ArrayList(listeners.keys)

		override fun getStartDelay() = animator.startDelay

		override fun setStartDelay(delayMS: Long) {
			animator.startDelay = delayMS
		}

		override fun isPaused() = animator.isPaused

		override fun isRunning() = animator.isRunning

		override fun isStarted() = animator.isStarted

		override fun removeAllListeners() {
			listeners.clear()
			animator.removeAllListeners()
		}

		override fun removeListener(listener: Animator.AnimatorListener) {
			listeners[listener]?.let {
				listeners.remove(listener)
				animator.removeListener(it)
			}
		}

		override fun setDuration(durationMS: Long) = animator.apply {
			duration = durationMS
		}

		override fun setTarget(target: Any?) = animator.setTarget(target)

		override fun setupEndValues() = animator.setupEndValues()

		override fun setupStartValues() = animator.setupStartValues()

	}

	private class AnimatorListenerWrapper internal constructor(
		private val animator: Animator,
		private val listener: Animator.AnimatorListener
	) : Animator.AnimatorListener {

		override fun onAnimationStart(animator: Animator) = listener.onAnimationStart(this.animator)

		override fun onAnimationEnd(animator: Animator) = listener.onAnimationEnd(this.animator)

		override fun onAnimationCancel(animator: Animator) = listener.onAnimationCancel(this.animator)

		override fun onAnimationRepeat(animator: Animator) = listener.onAnimationRepeat(this.animator)
	}

}
