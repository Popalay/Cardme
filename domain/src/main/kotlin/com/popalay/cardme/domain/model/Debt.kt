package com.popalay.cardme.domain.model

data class Debt(
        var id: String,
        var message: String,
        var createdAt: Long,
        var isTrash: Boolean,
        var holder: Holder
)