package com.popalay.cardme.presentation.base

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.popalay.cardme.R
import com.popalay.cardme.utils.animation.EndAnimatorListener
import kotlin.properties.Delegates

abstract class SlidingActivity : BaseActivity() {

    companion object {
        private val GESTURE_THRESHOLD = 10
        private val ANIMATION_DURATION = 200L
    }

    private var startX = 0f
    private var startY = 0f
    private var isSliding = false
    private lateinit var root: View
    private var screenSize: Point by Delegates.notNull<Point>()
    private var windowScrim: ColorDrawable by Delegates.notNull<ColorDrawable>()
    private var statusBarColor: Int = 0
    private var lastPos: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenSize = Point()
        windowManager.defaultDisplay.getSize(screenSize)
        statusBarColor = ContextCompat.getColor(this, R.color.primary_dark)
        windowScrim = ColorDrawable(Color.argb(0xE0, 0, 0, 0))
        windowScrim.alpha = 0
        window.setBackgroundDrawable(windowScrim)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        root = getRootView()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        var handled = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x
                startY = ev.y
                lastPos = startY
            }
            MotionEvent.ACTION_MOVE -> if (isSlidingDown(startX, startY, ev) && canSlideDown() || isSliding) {
                if (!isSliding) {
                    isSliding = true
                    window.statusBarColor = Color.TRANSPARENT
                    onSlidingStarted()
                    ev.action = MotionEvent.ACTION_CANCEL
                    super.dispatchTouchEvent(ev)
                }
                root.y = Math.max(ev.y / 1.5f - startY, 0f)
                updateScrim(lastPos > ev.y)
                handled = true
                lastPos = root.y
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (isSliding) {
                    isSliding = false
                    onSlidingFinished()
                    handled = true
                    if (shouldClose(root.y - startY)) {
                        closeDownAndDismiss()
                    } else {
                        toUp()
                    }
                }
                startX = 0f
                startY = 0f
            }
        }
        return handled || super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() = closeDownAndDismiss()

    protected abstract fun getRootView(): View

    protected fun onSlidingFinished() {
        //override to add logic
    }

    protected fun onSlidingStarted() {
        //override to add logic
    }

    protected open fun canSlideDown() = true

    private fun shouldClose(delta: Float): Boolean = delta > screenSize.y / 3

    private fun isSlidingDown(startX: Float, startY: Float, ev: MotionEvent): Boolean {
        val deltaX = Math.abs(startX - ev.x)
        if (deltaX > GESTURE_THRESHOLD) return false
        val deltaY = ev.y - startY
        return deltaY > GESTURE_THRESHOLD
    }

    private fun closeDownAndDismiss() {
        val start = root.y
        val finish = screenSize.y.toFloat()
        ObjectAnimator.ofFloat(root, "y", start, finish).apply {
            duration = ANIMATION_DURATION
            interpolator = DecelerateInterpolator()
            addListener(object : EndAnimatorListener() {
                override fun onAnimationEnd(animator: Animator) {
                    root.y = screenSize.y.toFloat()
                    updateScrim(false)
                    finish()
                }
            })
            addUpdateListener { updateScrim(false) }
        }.start()
    }

    private fun toUp() {
        val start = root.y
        val finish = 0f
        ObjectAnimator.ofFloat(root, "y", start, finish).apply {
            duration = ANIMATION_DURATION
            interpolator = DecelerateInterpolator()
            addListener(object : EndAnimatorListener() {
                override fun onAnimationEnd(animator: Animator) {
                    window.statusBarColor = statusBarColor
                }
            })
            addUpdateListener { updateScrim(true) }
        }.start()
    }

    private fun updateScrim(toTop: Boolean) {
        val progress = root.y / screenSize.y
        if (progress == 0f) window.statusBarColor = if (toTop) statusBarColor else Color.TRANSPARENT
        val alpha = (progress * 255f).toInt()
        windowScrim.alpha = 255 - alpha
        window.setBackgroundDrawable(windowScrim)
    }
}
