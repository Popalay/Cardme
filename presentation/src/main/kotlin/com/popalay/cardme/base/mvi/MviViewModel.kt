/*
 * Created by Ruslan on 09.02.18 16:04
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 09.02.18 16:04
 */

package com.popalay.cardme.base.mvi

import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Created by Ruslan Sierov on 09.02.18
 * Copyright (c) 2018. All right reserved
 */
interface MviViewModel<I, S> : Consumer<I> {

    val states: Observable<S>
}