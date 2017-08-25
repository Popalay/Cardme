package com.popalay.cardme.presentation.base

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
        private const val SWIPE_START_PART = 3
    }

    private var startX = 0F
    private var startY = 0F
    private var isSliding = false
    private lateinit var root: View
    private var screenSize: Point by Delegates.notNull<Point>()
    private var windowScrim: ColorDrawable by Delegates.notNull<ColorDrawable>()
    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenSize = Point()
        windowManager.defaultDisplay.getSize(screenSize)
        windowScrim = ColorDrawable(Color.argb(0xE0, 0, 0, 0))
        windowScrim.alpha = 0
        window.setBackgroundDrawable(windowScrim)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        root = getRootView()
        root.setBackgroundResource(R.color.window_background)
        val flingVelocity = ViewConfiguration.get(this).scaledMaximumFlingVelocity / 3F
        gestureDetector = GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(event1: MotionEvent, event2: MotionEvent,
                                 velocityX: Float, velocityY: Float): Boolean {
                return if (event2.x > event1.x && velocityX > flingVelocity && canSlideRight()) {
                    clearScrim()
                    finish()
                    true
                } else false
            }
        })
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
                    window.statusBarColor = Color.TRANSPARENT
                    onSlidingStarted()
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
                        closeRightAndDismiss()
                    } else {
                        toLeft()
                    }
                }
                startX = 0f
                startY = 0f
            }
        }
        return handled || super.dispatchTouchEvent(ev)
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.activity_right_left_exit)
    }

    protected abstract fun getRootView(): View

    protected open fun onSlidingFinished() = Unit

    protected open fun onSlidingStarted() = Unit

    protected open fun canSlideRight() = true

    private fun shouldClose(delta: Float): Boolean = delta > screenSize.x / SWIPE_START_PART

    private fun isSlidingRight(startX: Float, startY: Float, ev: MotionEvent): Boolean {
        val deltaY = Math.abs(ev.y - startY)
        val deltaX = ev.x - startX
        return deltaX > GESTURE_THRESHOLD && deltaY < GESTURE_THRESHOLD
    }

    private fun closeRightAndDismiss() {
        val start = root.x
        val finish = screenSize.x.toFloat()
        ObjectAnimator.ofFloat(root, "x", start, finish).apply {
            duration = ANIMATION_DURATION
            interpolator = DecelerateInterpolator()
            addListener(object : EndAnimatorListener {
                override fun onAnimationEnd(animator: Animator) {
                    finish()
                }
            })
            addUpdateListener { updateScrim() }
        }.start()
    }

    private fun toLeft() {
        val start = root.x
        val finish = 0F
        ObjectAnimator.ofFloat(root, "x", start, finish).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateInterpolator()
            addUpdateListener { updateScrim() }
        }.start()
    }

    private fun updateScrim() {
        val progress = root.x / screenSize.x
        val alpha = (progress * 255F).toInt()
        windowScrim.alpha = 255 - alpha
        window.setBackgroundDrawable(windowScrim)
    }

    private fun clearScrim() {
        windowScrim.alpha = 0
        window.setBackgroundDrawable(windowScrim)
    }
}
