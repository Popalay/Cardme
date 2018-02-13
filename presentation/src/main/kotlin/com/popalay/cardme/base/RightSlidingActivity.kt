package com.popalay.cardme.base

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GestureDetectorCompat
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.popalay.cardme.R
import com.popalay.cardme.utils.animation.EndAnimatorListener
import kotlin.properties.Delegates

abstract class RightSlidingActivity : BaseActivity() {

    companion object {

        private const val GESTURE_THRESHOLD = 10
        private const val ANIMATION_DURATION = 200L
        private const val SWIPE_START_PART = 0.3F
    }

    private var root: View by Delegates.notNull()
    private var screenSize: Point by Delegates.notNull()
    private var windowScrim: Drawable by Delegates.notNull()
    private var gestureDetector: GestureDetectorCompat by Delegates.notNull()
    private var rootElevation: Float by Delegates.notNull()
    private var startX = 0F
    private var startY = 0F
    private var isSliding = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenSize = Point()
        root = findViewById(android.R.id.content)
        root.setBackgroundResource(R.color.window_background)
        windowManager.defaultDisplay.getSize(screenSize)
        windowScrim = requireNotNull(ContextCompat.getDrawable(this, R.color.scrim))
        window.setBackgroundDrawable(windowScrim)
        rootElevation = resources.getDimension(R.dimen.small_elevation)
        val flingVelocity = ViewConfiguration.get(this).scaledMaximumFlingVelocity / 3F
        gestureDetector = GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                event1: MotionEvent,
                event2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ) = if (event2.x > event1.x && velocityX > flingVelocity && canSlideRight()) {
                doAfterSlide { supportFinishAfterTransition() }
                true
            } else {
                false
            }
        })
    }

    override fun finish() {
        doAfterSlide {
            super.finish()
            overridePendingTransition(0, 0)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (gestureDetector.onTouchEvent(ev)) return true
        var handled = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x
                startY = ev.y
            }
            MotionEvent.ACTION_MOVE -> if (isSlidingRight(startX, startY, ev) && canSlideRight() || isSliding) {
                if (!isSliding) {
                    isSliding = true
                    onSlidingStarted()
                    addElevation()
                    ev.action = MotionEvent.ACTION_CANCEL
                    super.dispatchTouchEvent(ev)
                }
                root.x = Math.max((ev.x - startX) / 1.5F, 0F)
                updateScrim()
                handled = true
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (isSliding) {
                    isSliding = false
                    onSlidingFinished()
                    handled = true
                    if (shouldClose(root.x)) {
                        doAfterSlide { supportFinishAfterTransition() }
                    } else {
                        toLeft()
                    }
                }
                startX = 0F
                startY = 0F
            }
        }
        return handled || super.dispatchTouchEvent(ev)
    }

    protected open fun onSlidingFinished() {}

    protected open fun onSlidingStarted() {}

    protected open fun canSlideRight() = true

    private fun shouldClose(delta: Float): Boolean = delta / screenSize.x > SWIPE_START_PART

    private fun isSlidingRight(startX: Float, startY: Float, ev: MotionEvent): Boolean {
        val deltaY = Math.abs(ev.y - startY)
        val deltaX = ev.x - startX
        return deltaX > GESTURE_THRESHOLD && deltaY < GESTURE_THRESHOLD
    }

    private fun doAfterSlide(block: () -> Unit) {
        val start = root.x
        val finish = screenSize.x.toFloat()
        ObjectAnimator.ofFloat(root, View.X, start, finish).apply {
            duration = ANIMATION_DURATION
            interpolator = DecelerateInterpolator()
            addListener(object : EndAnimatorListener {
                override fun onAnimationEnd(animator: Animator) {
                    block()
                }
            })
            addUpdateListener { updateScrim() }
        }.start()
    }

    private fun toLeft() {
        val start = root.x
        val finish = 0F
        ObjectAnimator.ofFloat(root, View.X, start, finish).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateInterpolator()
            addUpdateListener { updateScrim() }
            addListener(object : EndAnimatorListener {
                override fun onAnimationEnd(animator: Animator) {
                    removeElevation()
                }
            })
        }.start()
    }

    private fun updateScrim() {
        val progress = root.x / screenSize.x
        val alpha = (progress * 255F).toInt()
        windowScrim.alpha = 255 - alpha
        window.setBackgroundDrawable(windowScrim)
    }

    private fun addElevation(){
        root.elevation = rootElevation
    }

    private fun removeElevation(){
        root.elevation = 0F
    }
}