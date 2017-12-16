package com.popalay.cardme.domain.model

data class Card(
        val number: String = "",
        val title: String = "",
        val redactedNumber: String = "",
        val cardType: Long = 0,
        val generatedBackgroundSeed: Long = 0,
        val position: Int = 0,
        val isTrash: Boolean = false,
        val isPending: Boolean = false,
        val holderName: String = "",
        val iconRes: Int = 0,
        override val stableId: Long = number.hashCode().toLong()
) : StableId