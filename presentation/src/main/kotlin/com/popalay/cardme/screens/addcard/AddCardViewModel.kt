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
import com.popalay.cardme.domain.usecase.HolderNamesResult
import com.popalay.cardme.domain.usecase.HolderNamesUseCase
import com.popalay.cardme.domain.usecase.Result
import com.popalay.cardme.domain.usecase.SaveCardAction
import com.popalay.cardme.domain.usecase.SaveCardResult
import com.popalay.cardme.domain.usecase.SaveCardUseCase
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundAction
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundResult
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundUseCase
import com.popalay.cardme.domain.usecase.ValidateCardAction
import com.popalay.cardme.domain.usecase.ValidateCardResult
import com.popalay.cardme.domain.usecase.ValidateCardUseCase
import com.popalay.cardme.utils.extensions.notOfType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class AddCardViewModel @Inject constructor(
        private val router: CustomRouter,
        private val cardDetailsUseCase: CardDetailsUseCase,
        private val validateCardUseCase: ValidateCardUseCase,
        private val saveCardUseCase: SaveCardUseCase,
        private val shouldShowCardBackgroundUseCase: ShouldShowCardBackgroundUseCase,
        private val holderNamesUseCase: HolderNamesUseCase
) : MviViewModel<AddCardViewState, AddCardIntent>() {

    override val statesObservable = compose()

    private val intentFilter
        get() = IntentFilter<AddCardIntent> {
            it.publish {
                Observable.merge<AddCardIntent>(
                        it.ofType(AddCardIntent.Initial::class.java).take(1),
                        it.notOfType(AddCardIntent.Initial::class.java)
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
                        it.ofType(ValidateCardAction::class.java).compose(validateCardUseCase),
                        it.ofType(SaveCardAction::class.java).compose(saveCardUseCase)
                ))
            }
        }

    override fun actionFromIntent(intent: AddCardIntent): Action = when (intent) {
        is AddCardIntent.Initial.GetCard -> GetCardDetailsAction(intent.number)
        is AddCardIntent.Initial.GetHolderNames -> GetHolderNamesAction
        is AddCardIntent.Initial.GetShouldShowBackground -> ShouldShowCardBackgroundAction
        is AddCardIntent.NameChanged -> ValidateCardAction(currentState.card.copy(holderName = intent.name))
        is AddCardIntent.TitleChanged -> ValidateCardAction(currentState.card.copy(title = intent.title))
        is AddCardIntent.Accept -> SaveCardAction(currentState.card)
    }

    override fun compose(): Observable<AddCardViewState> = intentsSubject
            .compose(intentFilter)
            .map(this::actionFromIntent)
            .compose(actions)
            .scan(AddCardViewState.idle(), ::reduce)
            .replay(1)
            .autoConnect(0)
            .observeOn(AndroidSchedulers.mainThread())

    override fun reduce(oldState: AddCardViewState, result: Result): AddCardViewState = with(result) {
        when (this) {
            is CardDetailsResult -> when (this) {
                is CardDetailsResult.Success -> oldState.copy(card = card)
                is CardDetailsResult.Failure -> oldState.copy(error = throwable)
                is CardDetailsResult.Idle -> oldState
            }
            is HolderNamesResult -> when (this) {
                is HolderNamesResult.Success -> oldState.copy(holderNames = names)
                is HolderNamesResult.Failure -> oldState.copy(error = throwable)
                is HolderNamesResult.Idle -> oldState
            }
            is ShouldShowCardBackgroundResult -> when (this) {
                is ShouldShowCardBackgroundResult.Success -> oldState.copy(showBackground = show)
                is ShouldShowCardBackgroundResult.Failure -> oldState.copy(error = throwable)
                is ShouldShowCardBackgroundResult.Idle -> oldState
            }
            is ValidateCardResult -> when (this) {
                is ValidateCardResult.Success -> oldState.copy(canSave = valid, card = card)
                is ValidateCardResult.Failure -> oldState.copy(error = throwable, canSave = false)
                is ValidateCardResult.Idle -> oldState
            }
            is SaveCardResult -> when (this) {
                is SaveCardResult.Success -> {
                    router.exit() //TODO: think about this
                    oldState
                }
                is SaveCardResult.Failure -> oldState.copy(error = throwable)
                is SaveCardResult.Idle -> oldState
            }
            else -> throw IllegalStateException("Can not reduce state for result ${javaClass.name}")
        }
    }
}