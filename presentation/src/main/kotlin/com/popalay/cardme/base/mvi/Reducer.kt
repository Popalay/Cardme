/*
 * Created by popalay on 30.12.17 20:10
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 20:10
 */

package com.popalay.cardme.base.mvi

import com.popalay.cardme.domain.usecase.Result

interface Reducer<S : ViewState> {
    fun reduce(oldState: S, result: Result): S
}