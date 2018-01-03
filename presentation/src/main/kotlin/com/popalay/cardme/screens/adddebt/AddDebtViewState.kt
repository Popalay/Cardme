/*
 * Created by popalay on 03.01.18 23:07
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 30.12.17 22:10
 */

package com.popalay.cardme.screens.adddebt

import com.popalay.cardme.base.mvi.ViewState
import com.popalay.cardme.domain.model.Debt

data class AddDebtViewState(
        val debt: Debt,
        val holderNames: List<String>,
        val canSave: Boolean,
        val error: Throwable?
) : ViewState {
    companion object {
        fun idle() = AddDebtViewState(
                debt = Debt(),
                holderNames = listOf(),
                canSave = false,
                error = null
        )
    }
}