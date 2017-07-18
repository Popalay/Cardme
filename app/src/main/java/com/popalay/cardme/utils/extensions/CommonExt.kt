package com.popalay.cardme.utils.extensions

import android.support.v4.app.FragmentManager
import com.github.nitrico.lastadapter.StableId
import com.popalay.cardme.utils.recycler.DiffObservableList
import java.util.*

fun FragmentManager.currentFragment() = fragments?.filter { it.isVisible }?.firstOrNull()