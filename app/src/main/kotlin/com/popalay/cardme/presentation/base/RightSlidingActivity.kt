package com.popalay.cardme.presentation.base

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.popalay.cardme.R
import com.popalay.cardme.utils.animation.EndAnimatorListener
import kotlin.properties.Delegates

abstract class RightSlidingActivity : BaseActivity() {

    companion object {
        private val GESTURE_THRESHOLD = 10
        private val ANIMATION_DURATION = 200L
        private val SWIPE_START_PART = 2
    }

    private var startX = 0F
    private var startY = 0F
    private var isSliding = false
    private lateinit var root: View
    private var screenSize: Point by Delegates.notNull<Point>()
    private var windowScrim: ColorDrawable by Delegates.notNull<ColorDrawable>()

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
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        var handled = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x
                startY = ev.y
            }
            MotionEvent.ACTION_MOVE -> if (isSlidingRight(startX, startY, ev) && canSlideDown() || isSliding) {
                if (!isSliding) {
                    isSliding = true
                    window.statusBarColor = Color.TRANSPARENT
                    onSlidingStarted()
                    ev.action = MotionEvent.ACTION_CANCEL
                    super.dispatchTouchEvent(ev)
                }
                root.x = Math.max(ev.x - startX, 0f)
                updateScrim()
                handled = true
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (isSliding) {
                    isSliding = false
                    onSlidingFinished()
                    handled = true
                    if (shouldClose(root.x - startX)) {
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

    protected abstract fun getRootView(): View

    protected open fun onSlidingFinished() = Unit

    protected open fun onSlidingStarted() = Unit

    protected open fun canSlideDown() = true

    private fun shouldClose(delta: Float): Boolean = delta > screenSize.x / SWIPE_START_PART

    private fun isSlidingRight(startX: Float, startY: Float, ev: MotionEvent): Boolean {
        val deltaY = ev.y - startY
        if (deltaY > GESTURE_THRESHOLD) return false
        val deltaX = ev.x - startX
        return deltaX > GESTURE_THRESHOLD
    }

    private fun closeRightAndDismiss() {
        val start = root.x
        val finish = screenSize.x.toFloat()
        ObjectAnimator.ofFloat(root, "x", start, finish).apply {
            duration = ANIMATION_DURATION
            interpolator = DecelerateInterpolator()
            addListener(object : EndAnimatorListener {
                override fun onAnimationEnd(animator: Animator) {
                    root.x = screenSize.x.toFloat()
                    updateScrim()
                    finish()
                }
            })
            addUpdateListener { updateScrim() }
        }.start()
    }

    private fun toLeft() {
        val start = root.x
        val finish = 0f
        ObjectAnimator.ofFloat(root, "x", start, finish).apply {
            duration = ANIMATION_DURATION
            interpolator = DecelerateInterpolator()
            addUpdateListener { updateScrim() }
        }.start()
    }

    private fun updateScrim() {
        val progress = root.x / screenSize.x
        val alpha = (progress * 255f).toInt()
        windowScrim.alpha = 255 - alpha
        window.setBackgroundDrawable(windowScrim)
    }
}
