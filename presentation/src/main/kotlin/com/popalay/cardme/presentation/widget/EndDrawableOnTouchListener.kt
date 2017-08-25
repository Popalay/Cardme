package com.popalay.cardme.presentation.widget

import android.view.MotionEvent
import android.view.View
import android.widget.TextView

abstract class EndDrawableOnTouchListener : View.OnTouchListener {

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (view !is TextView) throw IllegalArgumentException("View must extends TextView")
            val offset = view.getRight() - view.compoundDrawables[DRAWABLE_END].bounds.width()
            if (event.rawX >= offset) return onDrawableTouch(event)
        }
        return false
    }

    abstract fun onDrawableTouch(event: MotionEvent): Boolean

    companion object {

        private val DRAWABLE_START = 0
        private val DRAWABLE_TOP = 1
        private val DRAWABLE_END = 2
        private val DRAWABLE_BOTTOM = 3
    }

}