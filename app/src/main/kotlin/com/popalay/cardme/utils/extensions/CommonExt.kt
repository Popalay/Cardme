package com.popalay.cardme.utils.extensions

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent


fun AppCompatActivity.currentFragment() = supportFragmentManager.fragments?.filter { it.isVisible }?.firstOrNull()

inline fun <reified T : Fragment> AppCompatActivity.findFragmentByType() = supportFragmentManager.fragments
        ?.filter { it is T }
        ?.map { it as T }
        ?.firstOrNull()

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
