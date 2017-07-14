package com.popalay.cardme.presentation.screens.cards

import io.card.payment.CreditCard
import io.reactivex.Observable

interface CardsViewModelFacade {

    fun doOnShareCard(): Observable<String>

    fun onCardScanned(creditCard: CreditCard)

}