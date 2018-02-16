package com.popalay.cardme.utils.extensions

import android.animation.Animator
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.RecyclerView
import android.transition.Transition
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.popalay.cardme.DURATION_SHORT
import com.popalay.cardme.utils.animation.EndAnimatorListener
import com.popalay.cardme.utils.animation.EndTransitionListener

fun BottomNavigationView.setSelectedItem(itemId: Int, notify: Boolean) {
    if (notify) {
        selectedItemId = itemId
    } else {
        (0 until menu.size())
                .map { menu.getItem(it) }
                .filter { it.itemId == itemId }
                .first().isChecked = true
    }
}

fun RecyclerView.onItemTouch(callback: (e: MotionEvent) -> Unit) = apply {
    addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
        override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent): Boolean {
            if (rv?.findChildViewUnder(e.x, e.y) != null) {
                callback(e)
            } else {
                e.action = MotionEvent.ACTION_CANCEL
                callback(e)
            }
            return false
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) = Unit

        override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) = Unit

    })
}

fun View.hideAnimated(delay: Long = 0L, endListener: (() -> Unit)? = null) {
    this.animate()
            .setStartDelay(delay)
            .scaleX(0F)
            .scaleY(0F)
            .alpha(0F)
            .setDuration(DURATION_SHORT)
            .setListener(object : EndAnimatorListener {
                override fun onAnimationEnd(p0: Animator?) {
                    endListener?.invoke()
                }
            })
            .start()
}

fun View.showAnimated(delay: Long = 0L, endListener: (() -> Unit)? = null) {
    this.animate()
            .setStartDelay(delay)
            .scaleX(1F)
            .scaleY(1F)
            .alpha(1F)
            .setDuration(DURATION_SHORT)
            .setListener(object : EndAnimatorListener {
                override fun onAnimationEnd(p0: Animator?) {
                    endListener?.invoke()
                }
            })
            .start()
}

fun Transition?.onEnd(block: () -> Unit) {
    this?.addListener(object : EndTransitionListener {
        override fun onTransitionEnd(transition: Transition?) {
            block()
            transition?.removeListener(this)
        }

    })
}

fun EditText.setTextIfNeeded(value: String) {
    if (text.isEmpty()) setText(value)
}