package com.popalay.cardme.data.models

import android.support.annotation.StringRes
import com.popalay.cardme.data.ExceptionFactory

class AppException(
        var errorType: ExceptionFactory.ErrorType?,
        @field:StringRes val messageRes: Int
) : RuntimeException()
