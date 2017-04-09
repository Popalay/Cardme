package com.popalay.cardme.ui.base;

import com.github.popalay.rxlifecyclemoxy.RxPresenter;

/**
 * Simplifies getting view. Assumes that we don't care if there are multiple views
 * attached and get the random one.
 */
public abstract class BasePresenter<T extends BaseView> extends RxPresenter<T> {

    public void handleBaseError(Throwable throwable) {
        throwable.printStackTrace();
    }

}
