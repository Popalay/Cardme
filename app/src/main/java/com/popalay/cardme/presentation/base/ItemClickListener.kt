package com.popalay.cardme.presentation.base

import com.github.nitrico.lastadapter.StableId

interface ItemClickListener<in T : StableId> {

    fun onItemClick(item: T)
}
