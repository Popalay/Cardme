package com.popalay.cardme.utils.extensions

import android.databinding.ObservableList
import android.util.Log
import com.popalay.cardme.utils.recycler.DiffObservableList
import io.reactivex.Flowable
import java.util.*

fun <T : Any> Flowable<List<T>>.setTo(list: ObservableList<in T>
): Flowable<List<T>> = doOnNext {
    if (list is DiffObservableList) {
        list.update(it)
    } else {
        list.clear()
        list.addAll(it)
    }
}

fun <T : Any> List<T>.swap(from: Int, to: Int) {
    if (this is DiffObservableList) {
        val tempList = ArrayList(this)
        Collections.swap(tempList, from, to)
        this.update(tempList)
    } else {
        Collections.swap(this, from, to)
    }
}

fun <T : Any> List<T>.swap(positions: Pair<Int, Int>) = swap(positions.first, positions.second)