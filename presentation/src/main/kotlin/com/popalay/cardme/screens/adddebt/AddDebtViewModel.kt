package com.popalay.cardme.screens.adddebt

import com.popalay.cardme.base.mvi.BaseMviViewModel
import com.popalay.cardme.base.mvi.LambdaProcessor
import com.popalay.cardme.base.mvi.LambdaReducer
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.domain.usecase.HolderNamesUseCase
import com.popalay.cardme.domain.usecase.SaveDebtUseCase
import com.popalay.cardme.domain.usecase.ValidateDebtUseCase
import javax.inject.Inject

class AddDebtViewModel @Inject constructor(
    private val router: CustomRouter,
    private val holderNamesUseCase: HolderNamesUseCase,
    private var validateDebtUseCase: ValidateDebtUseCase,
    private val saveDebtUseCase: SaveDebtUseCase
) : BaseMviViewModel<AddDebtViewState, AddDebtIntent>() {

    override val initialState = AddDebtViewState.idle()

    override val processor = LambdaProcessor<AddDebtIntent> {
        TODO()
    }

    override val reducer = LambdaReducer<AddDebtViewState> {
        when (this) {
            is HolderNamesUseCase.Result -> when (this) {
                is HolderNamesUseCase.Result.Success -> it.copy(holderNames = names)
                is HolderNamesUseCase.Result.Failure -> it.copy(error = throwable)
            }
            is ValidateDebtUseCase.Result -> when (this) {
                is ValidateDebtUseCase.Result.Success -> it.copy(canSave = valid)
                is ValidateDebtUseCase.Result.Invalid -> it.copy(error = throwable, canSave = false)
                is ValidateDebtUseCase.Result.Idle -> it.copy(debt = debt)
            }
            is SaveDebtUseCase.Result -> when (this) {
                is SaveDebtUseCase.Result.Failure -> it.copy(error = throwable)
                else -> it
            }
            else -> throw IllegalStateException("Can not reduce state for result ${javaClass.name}")
        }
    }
}
