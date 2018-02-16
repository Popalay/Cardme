/*
 * Created by Ruslan on 13.02.18 11:37
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 13.02.18 11:37
 */

package com.popalay.cardme.base.mvi

import android.arch.lifecycle.LifecycleOwner
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by Ruslan Sierov on 13.02.18
 * Copyright (c) 2018. All right reserved
 */
interface BindableMviView<S : ViewState, I : Intent> : MviView<S, I>, LifecycleOwner {

    fun bind(viewModel: MviViewModel<I, S>) {

        AndroidLifecycleScopeProvider.from(this).also {
            viewModel.states
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(it)
                .subscribe(this)

            intents
                .delaySubscription(viewModel.states.observeOn(AndroidSchedulers.mainThread()))
                .autoDisposable(it)
                .subscribe(viewModel)
        }
    }
}