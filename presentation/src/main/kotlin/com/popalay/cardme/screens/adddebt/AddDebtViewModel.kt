package com.popalay.cardme.screens.adddebt

import com.popalay.cardme.base.mvi.IntentFilter
import com.popalay.cardme.base.mvi.MviViewModel
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.domain.usecase.Action
import com.popalay.cardme.domain.usecase.ActionTransformer
import com.popalay.cardme.domain.usecase.GetHolderNamesAction
import com.popalay.cardme.domain.usecase.HolderNamesResult
import com.popalay.cardme.domain.usecase.HolderNamesUseCase
import com.popalay.cardme.domain.usecase.Result
import com.popalay.cardme.domain.usecase.SaveDebtAction
import com.popalay.cardme.domain.usecase.SaveDebtResult
import com.popalay.cardme.domain.usecase.SaveDebtUseCase
import com.popalay.cardme.domain.usecase.ValidateDebtAction
import com.popalay.cardme.domain.usecase.ValidateDebtResult
import com.popalay.cardme.domain.usecase.ValidateDebtUseCase
import com.popalay.cardme.utils.extensions.notOfType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class AddDebtViewModel @Inject constructor(
        private val router: CustomRouter,
        private val holderNamesUseCase: HolderNamesUseCase,
        private var validateDebtUseCase: ValidateDebtUseCase,
        private val saveDebtUseCase: SaveDebtUseCase
) : MviViewModel<AddDebtViewState, AddDebtIntent>() {

    override val intentFilter
        get() = IntentFilter<AddDebtIntent> {
            it.publish {
                Observable.merge<AddDebtIntent>(
                        it.ofType<AddDebtIntent.Initial.GetHolderNames>().take(1),
                        it.notOfType(AddDebtIntent.Initial::class.java)
                )
            }
        }

    override val actions
        get() = ActionTransformer {
            it.publish {
                Observable.merge(listOf(
                        it.ofType<GetHolderNamesAction>().compose(holderNamesUseCase),
                        it.ofType<ValidateDebtAction>().compose(validateDebtUseCase),
                        it.ofType<SaveDebtAction>().compose(saveDebtUseCase)
                ))
            }
        }

    override fun actionFromIntent(intent: AddDebtIntent): Action = when (intent) {
        is AddDebtIntent.Initial.GetHolderNames -> GetHolderNamesAction
        is AddDebtIntent.DebtHolderNameChanged -> ValidateDebtAction(intent.debt)
        is AddDebtIntent.DebtInformationChanged -> ValidateDebtAction(intent.debt)
        is AddDebtIntent.Accept -> SaveDebtAction(intent.debt)
    }

    override fun compose(): Observable<AddDebtViewState> = intentsSubject
            .compose(intentFilter)
            .map(::actionFromIntent)
            .compose(actions)
            .scan(AddDebtViewState.idle(), ::reduce)
            .replay(1)
            .autoConnect(0)
            .observeOn(AndroidSchedulers.mainThread())

    override fun reduce(oldState: AddDebtViewState, result: Result): AddDebtViewState = with(result) {
        when (this) {
            is HolderNamesResult -> when (this) {
                is HolderNamesResult.Success -> oldState.copy(holderNames = names)
                is HolderNamesResult.Failure -> oldState.copy(error = throwable)
            }
            is ValidateDebtResult -> when (this) {
                is ValidateDebtResult.Success -> oldState.copy(canSave = valid)
                is ValidateDebtResult.Failure -> oldState.copy(error = throwable, canSave = false)
                is ValidateDebtResult.Idle -> oldState.copy(debt = debt)
            }
            is SaveDebtResult -> when (this) {
                is SaveDebtResult.Success -> {
                    router.exit()
                    oldState
                }
                is SaveDebtResult.Failure -> oldState.copy(error = throwable)
                is SaveDebtResult.Idle -> oldState
            }
            else -> throw IllegalStateException("Can not reduce state for result ${javaClass.name}")
        }
    }
}
