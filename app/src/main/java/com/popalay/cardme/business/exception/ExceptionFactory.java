package com.popalay.cardme.business.exception;

import android.support.annotation.Nullable;

public final class ExceptionFactory {

    private ExceptionFactory() {
    }

    public enum ErrorType {
        CARD_EXIST,
        PERMISSION_DENIED
    }

    public static Throwable createError(ErrorType type) {
        return createError(type, null);
    }

    public static Throwable createError(ErrorType type, @Nullable String message) {
        return new AppException(type, message);
    }

    public static boolean isAppException(Throwable throwable) {
        return throwable instanceof AppException;
    }
}
