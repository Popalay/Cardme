package com.popalay.cardme.utils

fun String.firstLetters(): String {
    val splits = split(" ")
    return if (splits.isEmpty()) "" else splits.map { it[0].toString() }.reduce { acc, s -> acc + s }
}