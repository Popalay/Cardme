package com.popalay.cardme.utils.extensions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

fun FragmentManager.currentFragment() = fragments?.filter { it.isVisible }?.firstOrNull()

inline fun <reified T : Fragment> FragmentManager.findFragmentByType() = fragments
        ?.filter { it is T }
        ?.map { it as T }
        ?.firstOrNull()
