/*
 * Created by popalay on 10.12.17 18:57
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 10.12.17 18:57
 */

package com.popalay.cardme.utils.extensions

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

internal val Fragment.unsafeActivity: FragmentActivity
    get() = activity ?: throw IllegalStateException()

internal val Fragment.unsafeContext: Context
    get() = context ?: throw IllegalStateException()