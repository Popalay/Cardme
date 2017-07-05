package com.popalay.cardme.presentation.base.navigation;

import com.popalay.cardme.business.exception.AppException;

import ru.terrakok.cicerone.commands.Command;

/**
 * Shows app error.
 */
public class Error implements Command {

    private final AppException exception;

    public Error(AppException exception) {
        this.exception = exception;
    }

    public AppException getException() {
        return exception;
    }
}