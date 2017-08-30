package com.popalay.cardme.domain

import java.lang.RuntimeException

object ExceptionFactory {

    enum class ErrorType {
        CARD_EXIST,
        PERMISSION_DENIED
    }

    fun createError(type: ErrorType) = AppException(type)
}

class AppException(val errorType: ExceptionFactory.ErrorType) : RuntimeException()
