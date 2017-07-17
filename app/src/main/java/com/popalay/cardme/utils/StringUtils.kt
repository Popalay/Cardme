package com.popalay.cardme.utils

fun String.firstLetters(maxLetters: Int = 2): String {
    return if (this.isBlank()) "" else split(" ")
            .take(maxLetters)
            .map { it[0].toString() }
            .reduce { acc, s -> acc + s }
}

fun String.clean(): String {
    return this.trim().replace(Regex("\\s+"), " ")
}