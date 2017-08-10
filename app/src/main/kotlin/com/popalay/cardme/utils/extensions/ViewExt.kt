package com.popalay.cardme.utils.extensions

import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent

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