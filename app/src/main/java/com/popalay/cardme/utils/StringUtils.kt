package com.popalay.cardme.utils

fun String.firstLetters(): String = this.split(" ")
        .map { it[0].toString() }
        .reduce { acc, s -> acc + s }