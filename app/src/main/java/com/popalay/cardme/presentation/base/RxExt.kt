package com.popalay.cardme.presentation.base

import android.databinding.ObservableList
import io.reactivex.Flowable

fun <T : Any> Flowable<List<T>>.setTo(list: ObservableList<in T>
): Flowable<List<T>> = doOnNext {
    val newList = if (it != null) ArrayList<T>(it) else (emptyList<T>() as ArrayList<T>)
    list.clear()
    list.addAll(newList)
}