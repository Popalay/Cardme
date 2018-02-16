/*
 * Created by Ruslan on 09.02.18 17:31
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 19.01.18 16:18
 */

@file:Suppress("NOTHING_TO_INLINE")

package com.popalay.cardme.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by Ruslan Sierov on 18.01.18
 * Copyright (c) 2018. All right reserved
 */

inline fun <reified T> printEvent(tag: String, success: T?, error: Throwable?) = when {
    success == null && error == null -> System.out.println("$tag ---> Complete")
    success != null -> System.out.println("$tag ---> Success: $success")
    error != null -> System.out.println("$tag ---> Error: $error")
    else -> throw IllegalStateException("Cannot print log for events in this case!")
}

inline fun printEvent(tag: String, error: Throwable?) = when {
    error != null -> System.out.println("$tag ---> Error: $error")
    else -> System.out.println("$tag ---> Complete")
}

inline fun tag() = Thread.currentThread().stackTrace
    .first { it.fileName.endsWith(".kt") }
    .let { stack -> "${stack.fileName.removeSuffix(".kt")}::${stack.methodName} on ${Thread.currentThread().name}" }

inline fun <reified T> Single<T>.log(tag: String = tag()): Single<T> {
    return doOnEvent { success, error -> printEvent(tag, success, error) }
        .doOnSubscribe { System.out.println("$tag ---> Subscribe") }
        .doOnDispose { System.out.println("$tag ---> Dispose") }
}

inline fun <reified T> Maybe<T>.log(tag: String = tag()): Maybe<T> {
    return doOnEvent { success, error -> printEvent(tag, success, error) }
        .doOnSubscribe { System.out.println("$tag ---> Subscribe") }
        .doOnDispose { System.out.println("$tag ---> Dispose") }
}

inline fun Completable.log(tag: String = tag()): Completable {
    return doOnEvent { printEvent(tag, it) }
        .doOnSubscribe { System.out.println("$tag ---> Subscribe") }
        .doOnDispose { System.out.println("$tag ---> Dispose") }
}

inline fun <reified T> Observable<T>.log(tag: String = tag()): Observable<T> {
    return doOnEach { System.out.println("$tag ---> Each: $it") }
        .doOnSubscribe { System.out.println("$tag ---> Subscribe") }
        .doOnDispose { System.out.println("$tag ---> Dispose") }
}

inline fun <reified T> Flowable<T>.log(tag: String = tag()): Flowable<T> {
    return doOnEach { System.out.println("$tag ---> Each: $it") }
        .doOnSubscribe { System.out.println("$tag ---> Subscribe") }
        .doOnCancel { System.out.println("$tag ---> Cancel") }
}