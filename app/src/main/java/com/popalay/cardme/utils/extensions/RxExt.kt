package com.popalay.cardme.utils.extensions

import android.databinding.ObservableList
import com.popalay.cardme.DEBOUNCE_DELAY_MS
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import java.util.concurrent.TimeUnit

fun <T : Any> Flowable<List<T>>.setTo(list: ObservableList<in T>
): Flowable<List<T>> = doOnNext {
    val newList = ArrayList<T>(it)
    list.clear()
    list.addAll(newList)
}

fun <T : Any> Observable<List<T>>.setTo(list: ObservableList<in T>
): Observable<List<T>> = doOnNext {
    val newList = ArrayList<T>(it)
    list.clear()
    list.addAll(newList)
}

fun <T : Any> Flowable<T>.applyThrottling(): Flowable<T> = compose(applyThrottlingFlowable<T>())

fun <T : Any> Observable<T>.applyThrottling(): Observable<T> = compose(applyThrottlingObservable<T>())

private inline fun <T : Any> applyThrottlingObservable(): ObservableTransformer<T, T> = ObservableTransformer {
    it.throttleFirst(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS)
}

private inline fun <T : Any> applyThrottlingFlowable(): FlowableTransformer<T, T> = FlowableTransformer {
    it.throttleFirst(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS)
}
