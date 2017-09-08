package com.popalay.cardme.utils.recycler

import com.popalay.cardme.domain.model.StableId


class StableIdDiffListCallback<in T : StableId> : DiffObservableList.Callback<T> {

    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.stableId == newItem.stableId

    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem

}
