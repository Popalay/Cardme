/*
 * Created by popalay on 30.12.17 20:11
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 20:11
 */

package com.popalay.cardme.base.mvi

import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Created by Denys Nykyforov on 30.12.2017
 * Copyright (c) 2017. All right reserved
 */
interface MviView<S : ViewState, I : Intent> : Consumer<S> {

    val intents: Observable<I>
}