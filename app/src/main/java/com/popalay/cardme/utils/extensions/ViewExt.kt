package com.popalay.cardme.utils.extensions

import android.support.design.widget.BottomNavigationView

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