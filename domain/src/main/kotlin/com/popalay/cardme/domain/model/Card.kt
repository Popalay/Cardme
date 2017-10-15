package com.popalay.cardme.domain.model

class Card(
        var number: String = "",
        var title: String = "",
        var redactedNumber: String = "",
        var cardType: Long = 0,
        var generatedBackgroundSeed: Long = 0,
        var position: Int = 0,
        var isTrash: Boolean = false,
        var isPending: Boolean = false,
        var holderName: String = "",
        var iconRes: Int? = 0,
        override var stableId: Long? = number.hashCode().toLong()
) : StableId