package com.popalay.cardme.presentation.screens.debts

import android.os.Bundle
import io.reactivex.Observable

interface DebtsViewModelFacade {

    fun createAddDebtTransition(): Observable<Bundle>

}