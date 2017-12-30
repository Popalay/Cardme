/*
 * Created by popalay on 30.12.17 20:11
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 20:11
 */

package com.popalay.cardme.base.mvi

import io.reactivex.Observable

/**
 * Created by Denys Nykyforov on 30.12.2017
 * Copyright (c) 2017. All right reserved
 */
interface MviView<in S : ViewState, I : Intent> {

    fun intents(): Observable<I>

    fun render(state: S)
}