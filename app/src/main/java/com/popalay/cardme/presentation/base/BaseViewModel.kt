package com.popalay.cardme.presentation.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import com.popalay.cardme.App
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    protected val context: Context by lazy { app }
    protected val router: CustomRouter by lazy { App.getRouter() }
    protected val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    fun handleBaseError(throwable: Throwable) = throwable.printStackTrace()

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
