package com.popalay.cardme.data

import android.support.annotation.StringRes
import com.popalay.cardme.data.models.AppException

object ExceptionFactory {

    enum class ErrorType {
        CARD_EXIST,
        PERMISSION_DENIED
    }

    fun createError(type: ErrorType, @StringRes message: Int = 0) = AppException(type, message)
}
