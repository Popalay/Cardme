package com.popalay.cardme.utils.extensions

import com.popalay.cardme.DEBOUNCE_DELAY_MS
import io.reactivex.*
import java.util.concurrent.TimeUnit

fun <T : Any> Flowable<T>.applyThrottling(): Flowable<T> = compose(applyThrottlingFlowable<T>())

fun <T : Any> Observable<T>.applyThrottling(): Observable<T> = compose(applyThrottlingObservable<T>())

fun Any.toCompletable() = Completable.fromAction { this }

private fun <T : Any> applyThrottlingObservable(): ObservableTransformer<T, T> = ObservableTransformer {
    it.throttleFirst(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS)
}

private fun <T : Any> applyThrottlingFlowable(): FlowableTransformer<T, T> = FlowableTransformer {
    it.throttleFirst(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS)
}