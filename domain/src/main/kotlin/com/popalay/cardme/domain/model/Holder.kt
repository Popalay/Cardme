package com.popalay.cardme.domain.model

data class Holder(
        var name: String = "",
        var isTrash: Boolean = false,
        var isPending: Boolean = false,
        override var stableId: Long = name.hashCode().toLong()
) : StableId