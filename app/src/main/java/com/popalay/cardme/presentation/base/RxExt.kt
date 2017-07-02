package com.popalay.cardme.presentation.base

import android.databinding.ObservableList
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

fun <T : Any> Observable<List<T>?>.setTo(list: ObservableList<in T>
): Observable<List<T>?> = doOnNext {
    val newList = if (it != null) ArrayList<T>(it) else (emptyList<T>() as ArrayList<T>)
    list.clear()
    list.addAll(newList)
}

fun <T : Any?> Flowable<T>.bindSubscribe(
        composite: CompositeDisposable,
        onNext: (T) -> Unit = {},
        onError: (Throwable) -> Unit = { it.printStackTrace() },
        onComplete: () -> Unit = {}
) = composite.add(subscribe(onNext, onError, onComplete))

fun <T : Any?> Observable<T>.bindSubscribe(
        composite: CompositeDisposable,
        onNext: (T) -> Unit = {},
        onError: (Throwable) -> Unit = { it.printStackTrace() },
        onComplete: () -> Unit = {}
) = composite.add(subscribe(onNext, onError, onComplete))