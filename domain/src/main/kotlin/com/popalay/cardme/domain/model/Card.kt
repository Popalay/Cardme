package com.popalay.cardme.domain.model


class Card(
        var number: String,
        var title: String,
        var redactedNumber: String,
        val cardType: Long,
        var generatedBackgroundSeed: Long,
        var position: Int,
        var isTrash: Boolean,
        var holderName: String
)