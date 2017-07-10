package com.popalay.cardme.presentation.screens.cards

import io.reactivex.Observable

interface CardsViewModelFacade {

    fun doOnShareCard(): Observable<String>

}