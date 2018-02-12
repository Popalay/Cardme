/*
 * Created by popalay on 30.12.17 20:10
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 20:10
 */

package com.popalay.cardme.base.mvi

import com.popalay.cardme.domain.usecase.UseCase
import io.reactivex.functions.BiFunction

typealias Reducer<S> = BiFunction<S, UseCase.Result, S>

class LambdaReducer<S>(private val block: UseCase.Result.(oldState: S) -> S) : Reducer<S> {

    override fun apply(oldState: S, result: UseCase.Result): S = block(result, oldState)
}