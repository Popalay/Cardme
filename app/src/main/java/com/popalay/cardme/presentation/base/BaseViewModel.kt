package com.popalay.cardme.presentation.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import com.popalay.cardme.App
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    protected val context: Context by lazy { app }
    protected val router: Router by lazy { App.getRouter() }
    protected val disposables = CompositeDisposable()

    fun handleBaseError(throwable: Throwable) = throwable.printStackTrace()

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
