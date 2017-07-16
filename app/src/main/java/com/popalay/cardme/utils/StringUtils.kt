package com.popalay.cardme.utils

fun String.firstLetters(maxLetters: Int = 2): String {
    val splits = split(" ")
    return if (splits.isEmpty()) "" else splits
            .take(maxLetters)
            .map { it[0].toString() }
            .reduce { acc, s -> acc + s }
}