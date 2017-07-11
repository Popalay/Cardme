package com.popalay.cardme.utils.extensions

import android.databinding.ObservableArrayList
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

fun <E : Any?> ObservableArrayList<E>.swap(from: Int, to: Int) {
    val tmp = this[from]
    this[from] = this[to]
    this[to] = tmp
}

fun <E : Any?> ObservableArrayList<E>.swap(positions: Pair<Int, Int>) {
    swap(positions.first, positions.second)
}

fun FragmentManager.currentFragment(): Fragment? {
    return fragments?.filter { it.isVisible }?.firstOrNull()
}