package com.popalay.cardme.utils.extensions

import android.databinding.ObservableList
import com.popalay.cardme.DEBOUNCE_DELAY_MS
import com.popalay.cardme.utils.recycler.DiffObservableList
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import java.util.concurrent.TimeUnit

fun <T : Any> Flowable<T>.applyThrottling(): Flowable<T> = compose(applyThrottlingFlowable<T>())

fun <T : Any> Observable<T>.applyThrottling(): Observable<T> = compose(applyThrottlingObservable<T>())

private fun <T : Any> applyThrottlingObservable(): ObservableTransformer<T, T> = ObservableTransformer {
    it.throttleFirst(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS)
}

private fun <T : Any> applyThrottlingFlowable(): FlowableTransformer<T, T> = FlowableTransformer {
    it.throttleFirst(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS)
}