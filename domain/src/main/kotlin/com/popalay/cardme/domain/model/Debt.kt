package com.popalay.cardme.domain.model

data class Debt(
        var id: Long = 0L,
        var message: String = "",
        var createdAt: Long = 0L,
        var isTrash: Boolean = false,
        var holderName: String = "",
        override var stableId: Long = id
) : StableId