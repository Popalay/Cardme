package com.popalay.cardme.presentation.screens.holderdetails

import io.reactivex.Observable

interface HolderDetailsViewModelFacade {

    fun doOnShareCard(): Observable<String>

}