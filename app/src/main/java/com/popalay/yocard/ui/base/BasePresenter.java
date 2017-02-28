package com.popalay.yocard.ui.base;

import com.arellomobile.mvp.MvpPresenter;

/**
 * Simplifies getting view. Assumes that we don't care if there are multiple views
 * attached and get the random one.
 */
public abstract class BasePresenter<T extends BaseView> extends MvpPresenter<T> {

    public void handleBaseError(Throwable throwable) {
        throwable.printStackTrace();
    }

}
