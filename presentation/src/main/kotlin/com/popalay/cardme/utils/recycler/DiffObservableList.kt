package com.popalay.cardme.utils.recycler

import android.databinding.ListChangeRegistry
import android.databinding.ObservableList
import android.support.annotation.MainThread
import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import java.util.*
import kotlin.collections.ArrayList

class DiffObservableList<T : StableId> @JvmOverloads constructor(
        private val callback: Callback<T> = StableIdDiffListCallback<T>(),
        private val detectMoves: Boolean = true
) : AbstractList<T>(), ObservableList<T> {

    private val LIST_LOCK = Any()
    private var list = mutableListOf<T>()
    private val listeners = ListChangeRegistry()
    private val listCallback = ObservableListUpdateCallback()

    override val size: Int
        get() = list.size

    fun calculateDiff(newItems: List<T>): DiffUtil.DiffResult {
        var frozenList: ArrayList<T> = arrayListOf()
        synchronized(LIST_LOCK) {
            frozenList = ArrayList(list)
        }
        return doCalculateDiff(frozenList, newItems)
    }

    @MainThread fun update(newItems: List<T>, diffResult: DiffUtil.DiffResult) {
        synchronized(LIST_LOCK) {
            list = newItems.toMutableList()
        }
        diffResult.dispatchUpdatesTo(listCallback)
    }

    @MainThread fun update(newItems: List<T>) {
        val diffResult = doCalculateDiff(list, newItems)
        list = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(listCallback)
    }

    override fun addOnListChangedCallback(listener: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        listeners.add(listener)
    }

    override fun removeOnListChangedCallback(listener: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        listeners.remove(listener)
    }

    override fun get(index: Int): T {
        return list[index]
    }

    override fun set(index: Int, element: T): T {
        list[index] = element
        return element
    }

    private fun doCalculateDiff(oldItems: List<T>, newItems: List<T>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldItems[oldItemPosition]
                val newItem = newItems[newItemPosition]
                return callback.areItemsTheSame(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldItems[oldItemPosition]
                val newItem = newItems[newItemPosition]
                return callback.areContentsTheSame(oldItem, newItem)
            }
        }, detectMoves)
    }

    interface Callback<in T> {

        fun areItemsTheSame(oldItem: T, newItem: T): Boolean

        fun areContentsTheSame(oldItem: T, newItem: T): Boolean
    }

    internal inner class ObservableListUpdateCallback : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            listeners.notifyChanged(this@DiffObservableList, position, count)
        }

        override fun onInserted(position: Int, count: Int) {
            modCount += 1
            listeners.notifyInserted(this@DiffObservableList, position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            modCount += 1
            listeners.notifyRemoved(this@DiffObservableList, position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            listeners.notifyMoved(this@DiffObservableList, fromPosition, toPosition, 1)
        }
    }
}