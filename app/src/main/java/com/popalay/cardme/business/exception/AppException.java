package com.popalay.cardme.business.exception;

import android.support.annotation.Nullable;

public class AppException extends RuntimeException {

    private ExceptionFactory.ErrorType errorType;

    public AppException(ExceptionFactory.ErrorType errorType, @Nullable String message) {
        super(message);
        this.errorType = errorType;
    }

    public ExceptionFactory.ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ExceptionFactory.ErrorType errorType) {
        this.errorType = errorType;
    }
}
