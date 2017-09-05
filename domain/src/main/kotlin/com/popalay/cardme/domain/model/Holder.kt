package com.popalay.cardme.domain.model

data class Holder(
        var name: String,
        var isTrash: Boolean,
        var debts: List<Debt>
)