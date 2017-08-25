package com.popalay.cardme.domain

import java.lang.RuntimeException

object ExceptionFactory {

    enum class ErrorType {
        CARD_EXIST,
        PERMISSION_DENIED
    }

    fun createError(type: ErrorType, message: Int = 0) = AppException(type, message)
}

class AppException(
        val errorType: ExceptionFactory.ErrorType?,
        val messageRes: Int
) : RuntimeException()
