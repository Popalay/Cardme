package com.popalay.cardme.presentation.screens.holderdetails

import io.reactivex.disposables.Disposable

interface HolderDetailsViewModelFacade {

    fun doOnShareCard(body: (String) -> Unit): Disposable

}