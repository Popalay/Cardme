package com.popalay.cardme.presentation.base;

import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Simplifies getting view. Assumes that we don't care if there are multiple views
 * attached and get the random one.
 */
public abstract class BasePresenter<T extends BaseView> extends MvpPresenter<T> {

    private final CompositeDisposable disposables = new CompositeDisposable();

    public void handleBaseError(Throwable throwable) {
        throwable.printStackTrace();
    }

    protected void addDisposable(Disposable disposable) {
        if (disposable.isDisposed()) return;
        disposables.add(disposable);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}
