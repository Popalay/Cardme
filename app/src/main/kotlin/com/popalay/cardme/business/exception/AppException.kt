package com.popalay.cardme.business.exception

import android.support.annotation.StringRes

class AppException(
        var errorType: ExceptionFactory.ErrorType?,
        @field:StringRes val messageRes: Int
) : RuntimeException()
