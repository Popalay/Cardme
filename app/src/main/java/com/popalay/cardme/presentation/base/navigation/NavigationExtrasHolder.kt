package com.popalay.cardme.presentation.base.navigation

import io.card.payment.CreditCard
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationExtrasHolder @Inject constructor() {

    var holderId: String? = null

    var creditCard: CreditCard? = null
}
