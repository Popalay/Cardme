package com.popalay.cardme.presentation.screens.addcard

import io.reactivex.Observable

interface AddCardViewModelFacade {

    fun onShowCardExistsDialogDismiss()

    fun doOnShowCardExistsDialog(): Observable<Boolean>
}