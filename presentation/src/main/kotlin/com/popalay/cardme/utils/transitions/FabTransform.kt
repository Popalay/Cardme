/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.popalay.cardme.utils.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.transition.Transition
import android.transition.TransitionValues
import android.view.View
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import com.popalay.cardme.utils.animation.AnimUtils
import java.util.ArrayList

/**
 * A transition between a FAB & another surface using a circular reveal moving along an arc.
 *
 * See: https://www.google.com/design/spec/motion/transforming-material.html#transforming-material-radial-transformation
 */
class FabTransform(
	@ColorInt private val colorRes: Int,
	@DrawableRes private val iconRes: Int
) : Transition() {

	override fun getTransitionProperties() = TRANSITION_PROPERTIES

	override fun captureStartValues(transitionValues: TransitionValues) {
		captureValues(transitionValues)
	}

	override fun captureEndValues(transitionValues: TransitionValues) {
		captureValues(transitionValues)
	}

	override fun createAnimator(
		sceneRoot: ViewGroup,
		startValues: TransitionValues?,
		endValues: TransitionValues?
	): Animator? {
		if (startValues == null || endValues == null) return null

		val startBounds = startValues.values[PROP_BOUNDS] as Rect
		val endBounds = endValues.values[PROP_BOUNDS] as Rect

		val fromFab = endBounds.width() > startBounds.width()
		val view = endValues.view
		val dialogBounds = if (fromFab) endBounds else startBounds
		val fabBounds = if (fromFab) startBounds else endBounds
		val fastOutSlowInInterpolator = AnimUtils.getFastOutSlowInInterpolator(sceneRoot.context)
		val duration = duration
		val halfDuration = duration / 2
		val twoThirdsDuration = duration * 2 / 3

		if (!fromFab) {
			view.measure(
				makeMeasureSpec(startBounds.width(), View.MeasureSpec.EXACTLY),
				makeMeasureSpec(startBounds.height(), View.MeasureSpec.EXACTLY)
			)
			view.layout(startBounds.left, startBounds.top, startBounds.right, startBounds.bottom)
		}

		val translationX = startBounds.centerX() - endBounds.centerX()
		val translationY = startBounds.centerY() - endBounds.centerY()
		if (fromFab) {
			view.translationX = translationX.toFloat()
			view.translationY = translationY.toFloat()
		}

		val fabColor = ColorDrawable(colorRes)
		fabColor.setBounds(0, 0, dialogBounds.width(), dialogBounds.height())
		if (!fromFab) fabColor.alpha = 0
		view.overlay.add(fabColor)

		val fabIcon = ContextCompat.getDrawable(sceneRoot.context, iconRes)?.mutate()?.let {
			val iconLeft = (dialogBounds.width() - it.intrinsicWidth) / 2
			val iconTop = (dialogBounds.height() - it.intrinsicHeight) / 2
			it.setBounds(
				iconLeft, iconTop,
				iconLeft + it.intrinsicWidth,
				iconTop + it.intrinsicHeight
			)
			if (!fromFab) it.alpha = 0
			view.overlay.add(it)
		}

		val circularReveal: Animator
		if (fromFab) {
			circularReveal = ViewAnimationUtils.createCircularReveal(
				view,
				view.width / 2,
				view.height / 2,
				(startBounds.width() / 2).toFloat(),
				Math.hypot((endBounds.width() / 2).toDouble(), (endBounds.height() / 2).toDouble()).toFloat()
			)
			circularReveal.interpolator = AnimUtils.getFastOutLinearInInterpolator(sceneRoot.context)
		} else {
			circularReveal = ViewAnimationUtils.createCircularReveal(
				view,
				view.width / 2,
				view.height / 2,
				Math.hypot((startBounds.width() / 2).toDouble(), (startBounds.height() / 2).toDouble()).toFloat(),
				(endBounds.width() / 2).toFloat()
			)
			circularReveal.interpolator = AnimUtils.getLinearOutSlowInInterpolator(sceneRoot.context)

			// Persist the end clip i.e. stay at FAB size after the reveal has run
			circularReveal.addListener(object : AnimatorListenerAdapter() {
				override fun onAnimationEnd(animation: Animator) {
					view.outlineProvider = object : ViewOutlineProvider() {
						override fun getOutline(view: View, outline: Outline) {
							val left = (view.width - fabBounds.width()) / 2
							val top = (view.height - fabBounds.height()) / 2
							outline.setOval(left, top, left + fabBounds.width(), top + fabBounds.height())
							view.clipToOutline = true
						}
					}
				}
			})
		}
		circularReveal.duration = duration

		val translate = ObjectAnimator.ofFloat(
			view,
			View.TRANSLATION_X,
			View.TRANSLATION_Y,
			if (fromFab) pathMotion.getPath(translationX.toFloat(), translationY.toFloat(), 0f, 0f)
			else pathMotion.getPath(0f, 0f, (-translationX).toFloat(), (-translationY).toFloat())
		)
		translate.duration = duration
		translate.interpolator = fastOutSlowInInterpolator

		var fadeContents: MutableList<Animator>? = null
		if (view is ViewGroup) {
			fadeContents = ArrayList(view.childCount)
			for (i in view.childCount - 1 downTo 0) {
				val child = view.getChildAt(i)
				val fade = ObjectAnimator.ofFloat(child, View.ALPHA, if (fromFab) 1f else 0f)
				if (fromFab) {
					child.alpha = 0f
				}
				fade.duration = twoThirdsDuration
				fade.interpolator = fastOutSlowInInterpolator
				fadeContents.add(fade)
			}
		}

		val colorFade = ObjectAnimator.ofInt(fabColor, "alpha", if (fromFab) 0 else 255)
		val iconFade = ObjectAnimator.ofInt(fabIcon, "alpha", if (fromFab) 0 else 255)
		if (!fromFab) {
			colorFade.startDelay = halfDuration
			iconFade.startDelay = halfDuration
		}
		colorFade.duration = halfDuration
		iconFade.duration = halfDuration
		colorFade.interpolator = fastOutSlowInInterpolator
		iconFade.interpolator = fastOutSlowInInterpolator

		var elevation: Animator? = null
		if(!fromFab) {
			elevation = ObjectAnimator.ofFloat(view, View.TRANSLATION_Z, -view.elevation)
			elevation?.duration = duration
			elevation?.interpolator = fastOutSlowInInterpolator
		}

		val transition = AnimatorSet()
		transition.playTogether(circularReveal, translate, colorFade, iconFade)
		transition.playTogether(fadeContents)
		(elevation)?.let { transition.play(it) }
		if(fromFab) {
			transition.addListener(object : AnimatorListenerAdapter() {
				override fun onAnimationEnd(animation: Animator) {
					view.overlay.clear()
				}
			})
		}
		return AnimUtils.NoPauseAnimator(transition)
	}

	private fun captureValues(transitionValues: TransitionValues) {
		val view = transitionValues.view
		if (view == null || view.width <= 0 || view.height <= 0) return
		transitionValues.values[PROP_BOUNDS] = Rect(view.left, view.top, view.right, view.bottom)
	}

	companion object {

		private const val EXTRA_FAB_COLOR = "EXTRA_FAB_COLOR"
		private const val EXTRA_FAB_ICON_RES_ID = "EXTRA_FAB_ICON_RES_ID"
		private const val DEFAULT_DURATION = 240L
		private const val PROP_BOUNDS = "cardme:fabTransform:bounds"
		private val TRANSITION_PROPERTIES = arrayOf(PROP_BOUNDS)

		fun addExtras(intent: Intent, @ColorInt fabColor: Int, @DrawableRes fabIconResId: Int) {
			intent.putExtra(EXTRA_FAB_COLOR, fabColor)
			intent.putExtra(EXTRA_FAB_ICON_RES_ID, fabIconResId)
		}

		fun setup(activity: Activity, target: View?): Boolean {
			val intent = activity.intent
			if (!intent.hasExtra(EXTRA_FAB_COLOR) || !intent.hasExtra(EXTRA_FAB_ICON_RES_ID)) {
				return false
			}

			val color = intent.getIntExtra(EXTRA_FAB_COLOR, Color.TRANSPARENT)
			val icon = intent.getIntExtra(EXTRA_FAB_ICON_RES_ID, -1)
			val sharedEnter = FabTransform(color, icon)
			target?.let { sharedEnter.addTarget(target) }
			activity.window.sharedElementEnterTransition = sharedEnter
			return true
		}
	}

	init {
		pathMotion = GravityArcMotion()
		duration = DEFAULT_DURATION
	}
}