package com.popalay.cardme.screens.addcard

import com.popalay.cardme.base.mvi.BaseMviViewModel
import com.popalay.cardme.base.mvi.IntentProcessor
import com.popalay.cardme.base.mvi.LambdaReducer
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.domain.usecase.CardDetailsUseCase
import com.popalay.cardme.domain.usecase.HolderNamesUseCase
import com.popalay.cardme.domain.usecase.SaveCardUseCase
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundUseCase
import com.popalay.cardme.domain.usecase.ValidateCardUseCase
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class AddCardViewModel @Inject constructor(
    private val router: CustomRouter,
    private val cardDetailsUseCase: CardDetailsUseCase,
    private val validateCardUseCase: ValidateCardUseCase,
    private val saveCardUseCase: SaveCardUseCase,
    private val shouldShowCardBackgroundUseCase: ShouldShowCardBackgroundUseCase,
    private val holderNamesUseCase: HolderNamesUseCase
) : BaseMviViewModel<AddCardViewState, AddCardIntent>() {

    override val initialState = AddCardViewState.idle()

    override val processor = IntentProcessor<AddCardIntent> { intents ->
        listOf(
            intents.ofType<AddCardIntent.Initial.GetCard>()
                .take(1)
                .map { CardDetailsUseCase.Action(it.number) }
                .compose(cardDetailsUseCase),
            intents.ofType<AddCardIntent.Initial.GetShouldShowBackground>()
                .take(1)
                .map { ShouldShowCardBackgroundUseCase.Action }
                .compose(shouldShowCardBackgroundUseCase),
            intents.ofType<AddCardIntent.CardNameChanged>()
                .map { HolderNamesUseCase.Action(it.card.holderName) }
                .compose(holderNamesUseCase),
            intents.ofType<AddCardIntent.CardNameChanged>()
                .map { ValidateCardUseCase.Action(it.card) }
                .compose(validateCardUseCase),
            intents.ofType<AddCardIntent.CardTitleChanged>()
                .map { ValidateCardUseCase.Action(it.card) }
                .compose(validateCardUseCase),
            intents.ofType<AddCardIntent.Accept>()
                .map { SaveCardUseCase.Action(it.card) }
                .compose(saveCardUseCase)
                .doOnNext { if (it == SaveCardUseCase.Result.Success) router.exit() }
        )
    }

    override val reducer = LambdaReducer<AddCardViewState> { state ->
        when (this) {
            is CardDetailsUseCase.Result -> when (this) {
                is CardDetailsUseCase.Result.Success -> state.copy(card = card)
                is CardDetailsUseCase.Result.Failure -> state.copy(error = throwable)
                CardDetailsUseCase.Result.Idle -> state
            }
            is HolderNamesUseCase.Result -> when (this) {
                is HolderNamesUseCase.Result.Success -> state.copy(holderNames = names)
                is HolderNamesUseCase.Result.Failure -> state.copy(error = throwable)
                else -> state
            }
            is ShouldShowCardBackgroundUseCase.Result -> when (this) {
                is ShouldShowCardBackgroundUseCase.Result.Success -> state.copy(showBackground = show)
                is ShouldShowCardBackgroundUseCase.Result.Failure -> state.copy(error = throwable)
            }
            is ValidateCardUseCase.Result -> when (this) {
                is ValidateCardUseCase.Result.Valid -> state.copy(card = card, canSave = true)
                is ValidateCardUseCase.Result.Invalid -> state.copy(card = card, canSave = false)
            }
            is SaveCardUseCase.Result -> when (this) {
                is SaveCardUseCase.Result.Failure -> state.copy(error = throwable)
                else -> state
            }
            else -> throw IllegalStateException("Can not reduce state for result ${javaClass.name}")
        }
    }
}