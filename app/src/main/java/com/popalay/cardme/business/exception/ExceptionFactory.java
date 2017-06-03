package com.popalay.cardme.business.exception;

import android.support.annotation.StringRes;

import com.popalay.cardme.R;

public final class ExceptionFactory {

    private ExceptionFactory() {
    }

    public enum ErrorType {
        CARD_EXIST,
        PERMISSION_DENIED
    }

    public static Throwable createError(ErrorType type) {
        return createError(type, R.string.none);
    }

    public static Throwable createError(ErrorType type, @StringRes int message) {
        return new AppException(type, message);
    }

    public static boolean isAppException(Throwable throwable) {
        return throwable instanceof AppException;
    }
}
