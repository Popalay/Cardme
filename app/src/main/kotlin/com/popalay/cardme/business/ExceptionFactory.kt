package com.popalay.cardme.business

import android.support.annotation.StringRes

object ExceptionFactory {

    enum class ErrorType {
        CARD_EXIST,
        PERMISSION_DENIED
    }

    fun createError(type: ErrorType, @StringRes message: Int = 0) = AppException(type, message)
}

class AppException(
        val errorType: ExceptionFactory.ErrorType?,
        @field:StringRes val messageRes: Int
) : RuntimeException()
