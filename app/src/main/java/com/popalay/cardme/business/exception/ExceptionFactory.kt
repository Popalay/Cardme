package com.popalay.cardme.business.exception

import android.support.annotation.StringRes

import com.popalay.cardme.R

object ExceptionFactory {

    enum class ErrorType {
        CARD_EXIST,
        PERMISSION_DENIED
    }

    fun createError(type: ErrorType, @StringRes message: Int = R.string.none) = AppException(type, message)

    fun isAppException(throwable: Throwable) = throwable is AppException
}
