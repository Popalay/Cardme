package com.popalay.cardme.domain.model

data class Settings(
        var id: Long = 0L,
        var language: String = "",
        var theme: String = "",
        var isCardBackground: Boolean = true
)
