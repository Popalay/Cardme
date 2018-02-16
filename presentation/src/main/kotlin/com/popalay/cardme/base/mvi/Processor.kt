/*
 * Created by Ruslan on 10.02.18 18:03
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 10.02.18 18:03
 */

package com.popalay.cardme.base.mvi

import com.popalay.cardme.domain.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Created by Ruslan Sierov on 10.02.18
 * Copyright (c) 2018. All right reserved
 */

interface Processor<I> : ObservableTransformer<I, UseCase.Result>

class IntentProcessor<I : Intent>(
    private val block: (sharedIntents: Observable<I>) -> List<Observable<out UseCase.Result>>
) : Processor<I> {

    override fun apply(intents: Observable<I>): Observable<UseCase.Result> =
        intents.publish { Observable.merge(block(it)) }
}