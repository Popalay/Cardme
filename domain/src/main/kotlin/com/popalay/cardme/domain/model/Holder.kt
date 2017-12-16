package com.popalay.cardme.domain.model

data class Holder(
        val name: String = "",
        val isTrash: Boolean = false,
        val cardsCount: Int = 0,
        val debtsCount: Int = 0,
        override val stableId: Long = name.hashCode().toLong()
) : StableId