package com.popalay.cardme.utils.extensions

import com.popalay.cardme.DEBOUNCE_DELAY_MS
import com.uber.autodispose.ObservableSubscribeProxy
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.OnErrorNotImplementedException
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T : Any> Flowable<T>.applyThrottling(): Flowable<T> = compose(applyThrottlingFlowable<T>())

fun <T : Any> Observable<T>.applyThrottling(): Observable<T> = compose(applyThrottlingObservable<T>())

private fun <T : Any> applyThrottlingObservable(): ObservableTransformer<T, T> = ObservableTransformer {
	it.throttleFirst(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS, Schedulers.computation())
}

private fun <T : Any> applyThrottlingFlowable(): FlowableTransformer<T, T> = FlowableTransformer {
	it.throttleFirst(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS, Schedulers.computation())
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T : Any, U : Any> Observable<T>.notOfType(clazz: Class<U>): Observable<T> {
	checkNotNull(clazz) { "clazz is null" }
	return filter { !clazz.isInstance(it) }
}

/**
 * Overloaded subscribe function that allows passing named parameters
 */
fun <T : Any> ObservableSubscribeProxy<T>.subscribeBy(
	onError: (Throwable) -> Unit = { throw OnErrorNotImplementedException(it) },
	onComplete: () -> Unit = {},
	onNext: (T) -> Unit = {}
): Disposable = subscribe(onNext, onError, onComplete)