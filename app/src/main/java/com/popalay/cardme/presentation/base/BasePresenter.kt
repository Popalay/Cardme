package com.popalay.cardme.presentation.base

import com.arellomobile.mvp.MvpPresenter

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Simplifies getting view. Assumes that we don't care if there are multiple views
 * attached and get the random one.
 */
abstract class BasePresenter<T : BaseView> : MvpPresenter<T>() {

    private val disposables = CompositeDisposable()

    fun handleBaseError(throwable: Throwable) = throwable.printStackTrace()

    protected fun addDisposable(disposable: Disposable) {
        if (disposable.isDisposed) return
        disposables.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
