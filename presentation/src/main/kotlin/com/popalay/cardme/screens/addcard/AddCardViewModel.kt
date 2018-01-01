package com.popalay.cardme.screens.addcard

import com.popalay.cardme.base.mvi.IntentFilter
import com.popalay.cardme.base.mvi.MviViewModel
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.domain.usecase.Action
import com.popalay.cardme.domain.usecase.ActionTransformer
import com.popalay.cardme.domain.usecase.CardDetailsResult
import com.popalay.cardme.domain.usecase.CardDetailsUseCase
import com.popalay.cardme.domain.usecase.GetCardDetailsAction
import com.popalay.cardme.domain.usecase.GetHolderNamesAction
import com.popalay.cardme.domain.usecase.HolderNamesUseCase
import com.popalay.cardme.domain.usecase.Result
import com.popalay.cardme.domain.usecase.SaveCardAction
import com.popalay.cardme.domain.usecase.SaveCardResult
import com.popalay.cardme.domain.usecase.SaveCardUseCase
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundAction
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundUseCase
import com.popalay.cardme.domain.usecase.ValidateCardAction
import com.popalay.cardme.domain.usecase.ValidateCardResult
import com.popalay.cardme.domain.usecase.ValidateCardUseCase
import com.popalay.cardme.utils.extensions.notOfType
import io.reactivex.Observable
import javax.inject.Inject

class AddCardViewModel @Inject constructor(
        private val router: CustomRouter,
        private val cardDetailsUseCase: CardDetailsUseCase,
        private val validateCardUseCase: ValidateCardUseCase,
        private val saveCardUseCase: SaveCardUseCase,
        private val shouldShowCardBackgroundUseCase: ShouldShowCardBackgroundUseCase,
        private val holderNamesUseCase: HolderNamesUseCase
) : MviViewModel<AddCardViewState, AddCardIntent>() {

    private val intentFilter
        get() = IntentFilter<AddCardIntent> {
            it.publish {
                Observable.merge<AddCardIntent>(
                        it.ofType(AddCardIntent.InitialGetCard::class.java).take(1),
                        it.notOfType(AddCardIntent.InitialGetCard::class.java)
                )
            }
        }

    private val actions
        get() = ActionTransformer {
            it.publish {
                Observable.merge(listOf(
                        it.ofType(GetCardDetailsAction::class.java).compose(cardDetailsUseCase),
                        it.ofType(ShouldShowCardBackgroundAction::class.java).compose(shouldShowCardBackgroundUseCase),
                        it.ofType(GetHolderNamesAction::class.java).compose(holderNamesUseCase),
                        it.ofType(GetCardDetailsAction::class.java).compose(cardDetailsUseCase),
                        it.ofType(ValidateCardAction::class.java).compose(validateCardUseCase),
                        it.ofType(SaveCardAction::class.java).compose(saveCardUseCase)
                ))
            }
        }

    override fun reduce(oldState: AddCardViewState, result: Result): AddCardViewState = when (result) {
        is CardDetailsResult -> when (result) {
            is CardDetailsResult.Success -> oldState.copy(card = result.card)
            is CardDetailsResult.Failure -> oldState.copy(error = result.throwable)
            is CardDetailsResult.Idle -> oldState
        }
        is ValidateCardResult -> when (result) {
            is ValidateCardResult.Success -> oldState.copy(canSave = result.valid)
            is ValidateCardResult.Failure -> oldState.copy(error = result.throwable)
            is ValidateCardResult.Idle -> oldState
        }
        is SaveCardResult -> when (result) {
            is SaveCardResult.Success -> {
                router.exit() //TODO: think about this
                oldState
            }
            is SaveCardResult.Failure -> oldState.copy(error = result.throwable)
            is SaveCardResult.Idle -> oldState
        }
        else -> throw IllegalStateException("Can not reduce state for result ${result.javaClass.name}")
    }

    override fun actionFromIntent(intent: AddCardIntent): Action = when (intent) {
        is AddCardIntent.InitialGetCard -> GetCardDetailsAction(intent.number)
        is AddCardIntent.InitialGetHolderNames -> GetHolderNamesAction
        is AddCardIntent.InitialGetShoulsShowBackground -> ShouldShowCardBackgroundAction
        is AddCardIntent.NameChanged -> ValidateCardAction(intent.name) //TODO: set name into state
        is AddCardIntent.TitleChanged -> TODO() //TODO: set title into state
        is AddCardIntent.Accept -> SaveCardAction(currentState.card)
    }

    override fun compose(): Observable<AddCardViewState> = intentsSubject
            .compose(intentFilter)
            .map(this::actionFromIntent)
            .compose(actions)
            .scan(AddCardViewState.idle(), ::reduce)
            .replay(1)
            .autoConnect(0)
}