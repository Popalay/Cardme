package com.popalay.cardme.domain.model

data class Holder(
        var name: String = "",
        var isTrash: Boolean = false,
        var debts: List<Debt> = listOf(),
        override var stableId: Long = name.hashCode().toLong()
) : StableId