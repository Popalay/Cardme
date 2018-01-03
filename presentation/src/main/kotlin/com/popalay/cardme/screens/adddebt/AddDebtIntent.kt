/*
 * Created by popalay on 03.01.18 23:05
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 03.01.18 22:02
 */

package com.popalay.cardme.screens.adddebt

import com.popalay.cardme.base.mvi.Intent
import com.popalay.cardme.domain.model.Debt

sealed class AddDebtIntent : Intent {

    sealed class Initial : AddDebtIntent() {
        object GetHolderNames : AddDebtIntent.Initial()
    }

    data class DebtHolderNameChanged(val debt: Debt) : AddDebtIntent()
    data class DebtInformationChanged(val debt: Debt) : AddDebtIntent()
    data class Accept(val debt: Debt) : AddDebtIntent()
}