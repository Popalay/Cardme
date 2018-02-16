/*
 * Created by popalay on 30.12.17 20:08
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 20:08
 */

package com.popalay.cardme.base.mvi

import com.popalay.cardme.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

abstract class BaseMviViewModel<S : ViewState, I : Intent>
    : BaseViewModel(), MviViewModel<I, S> {

    override val states: Observable<S> by lazy(LazyThreadSafetyMode.NONE) {
        intentsSubject
            .hide()
            .observeOn(Schedulers.computation())
            .compose(processor)
            .scan(initialState, reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(1) { disposables.add(it) }
    }

    protected abstract val initialState: S

    protected abstract val processor: Processor<I>

    protected abstract val reducer: Reducer<S>

    private val intentsSubject: PublishSubject<I> = PublishSubject.create<I>()

    override fun accept(intent: I) {
        intentsSubject.onNext(intent)
    }
}