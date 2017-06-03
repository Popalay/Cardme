package com.popalay.cardme.business.exception;

import android.support.annotation.StringRes;

public class AppException extends RuntimeException {

    private ExceptionFactory.ErrorType errorType;
    @StringRes private final int messageRes;

    public AppException(ExceptionFactory.ErrorType errorType, @StringRes int message) {
        this.messageRes = message;
        this.errorType = errorType;
    }

    public ExceptionFactory.ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ExceptionFactory.ErrorType errorType) {
        this.errorType = errorType;
    }

    public int getMessageRes() {
        return messageRes;
    }
}
