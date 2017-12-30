/*
 * Created by popalay on 30.12.17 20:08
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 20:08
 */

package com.popalay.cardme.base.mvi

import com.popalay.cardme.base.BaseViewModel
import com.popalay.cardme.domain.usecase.Action
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class MviViewModel<S : ViewState, I : Intent> : BaseViewModel(), Reducer<S>, StateProvider<S> {

    protected val intentsSubject: PublishSubject<I> = PublishSubject.create()

    protected val currentState: S get() = statesObservable.blockingLast()

    @Suppress("LeakingThis")
    private val statesObservable: Observable<S> = compose()

    override fun states(): Observable<S> = statesObservable

    @Suppress("unused")
    fun processIntents(intents: Observable<I>) {
        intents.subscribe(intentsSubject)
    }

    protected abstract fun actionFromIntent(intent: I): Action

    protected abstract fun compose(): Observable<S>
}