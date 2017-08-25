package com.popalay.cardme.presentation.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel() : ViewModel() {

    protected val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    fun handleBaseError(throwable: Throwable) = throwable.printStackTrace()

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
