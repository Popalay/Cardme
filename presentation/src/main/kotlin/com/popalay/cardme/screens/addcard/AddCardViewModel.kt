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

    override val processor = IntentProcessor<AddCardIntent> {
        listOf(
            it.ofType<AddCardIntent.Initial.GetCard>()
                .take(1)
                .map { CardDetailsUseCase.Action(it.number) }
                .compose(cardDetailsUseCase),
            it.ofType<AddCardIntent.Initial.GetShouldShowBackground>()
                .take(1)
                .map { ShouldShowCardBackgroundUseCase.Action }
                .compose(shouldShowCardBackgroundUseCase),
            it.ofType<AddCardIntent.CardNameChanged>()
                .map { HolderNamesUseCase.Action(it.card.holderName) }
                .compose(holderNamesUseCase),
            it.ofType<AddCardIntent.CardNameChanged>()
                .map { ValidateCardUseCase.Action(it.card) }
                .compose(validateCardUseCase),
            it.ofType<AddCardIntent.CardTitleChanged>()
                .map { ValidateCardUseCase.Action(it.card) }
                .compose(validateCardUseCase),
            it.ofType<AddCardIntent.Accept>()
                .map { SaveCardUseCase.Action(it.card) }
                .compose(saveCardUseCase)
                .doOnNext { if (it == SaveCardUseCase.Result.Success) router.exit() }
        )
    }

    override val reducer = LambdaReducer<AddCardViewState> {
        when (this) {
            is CardDetailsUseCase.Result -> when (this) {
                is CardDetailsUseCase.Result.Success -> it.copy(card = card)
                is CardDetailsUseCase.Result.Failure -> it.copy(error = throwable)
                CardDetailsUseCase.Result.Idle -> it
            }
            is HolderNamesUseCase.Result -> when (this) {
                is HolderNamesUseCase.Result.Success -> it.copy(holderNames = names)
                is HolderNamesUseCase.Result.Failure -> it.copy(error = throwable)
                else -> it
            }
            is ShouldShowCardBackgroundUseCase.Result -> when (this) {
                is ShouldShowCardBackgroundUseCase.Result.Success -> it.copy(showBackground = show)
                is ShouldShowCardBackgroundUseCase.Result.Failure -> it.copy(error = throwable)
            }
            is ValidateCardUseCase.Result -> when (this) {
                is ValidateCardUseCase.Result.Valid -> it.copy(card = card, canSave = true)
                is ValidateCardUseCase.Result.Invalid -> it.copy(card = card, canSave = false)
            }
            is SaveCardUseCase.Result -> when (this) {
                is SaveCardUseCase.Result.Failure -> it.copy(error = throwable)
                else -> it
            }
            else -> throw IllegalStateException("Can not reduce state for result ${javaClass.name}")
        }
    }
}