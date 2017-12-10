package com.popalay.cardme.domain.model

data class Holder(
        var name: String = "",
        var isTrash: Boolean = false,
        var cardsCount: Int = 0,
        var debtsCount: Int = 0,
        override var stableId: Long = name.hashCode().toLong()
) : StableId