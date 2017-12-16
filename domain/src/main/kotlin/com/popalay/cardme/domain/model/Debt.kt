package com.popalay.cardme.domain.model

data class Debt(
        val id: Long = 0L,
        val message: String = "",
        val createdAt: Long = 0L,
        val isTrash: Boolean = false,
        val holderName: String = "",
        override val stableId: Long = id
) : StableId