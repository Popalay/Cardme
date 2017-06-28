package com.popalay.cardme.presentation.base

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription

fun <T : Any> Observable<T>.setTo(field: ObservableField<in T>): Observable<T> = doOnNext { field.set(it) }

fun Observable<Boolean>.setTo(field: ObservableBoolean): Observable<Boolean> = doOnNext { field.set(it) }

fun <T : Any> Observable<List<T>>.setTo(list: ObservableList<in T>
): Observable<List<T>> = doOnNext {
    val newList = if (it != null) ArrayList<T>(it) else (emptyList<T>() as ArrayList<T>)
    list.clear()
    list.addAll(newList)
}

fun <T : Any> Observable<T>.bindSubscribe(
        composite: CompositeSubscription,
        onNext: (T) -> Unit = {},
        onError: (Throwable) -> Unit = { it.printStackTrace() },
        onComplete: () -> Unit = {}
) = composite.add(subscribe(onNext, onError, onComplete))

fun <T : Any> Observable<T>.toMain(): Observable<T> = observeOn(AndroidSchedulers.mainThread())