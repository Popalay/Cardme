package com.popalay.cardme.presentation.screens.addcard

import io.reactivex.disposables.Disposable

interface AddCardViewModelFacade {

    fun onShowCardExistsDialogDismiss()

    fun doOnShowCardExistsDialog(body: () -> Unit): Disposable

}