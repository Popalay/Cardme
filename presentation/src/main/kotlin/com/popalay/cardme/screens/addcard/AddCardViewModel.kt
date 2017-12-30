package com.popalay.cardme.screens.addcard

import com.popalay.cardme.base.mvi.Intent
import com.popalay.cardme.base.mvi.MviViewModel
import com.popalay.cardme.base.mvi.ViewState
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.usecase.Action
import com.popalay.cardme.domain.usecase.CardDetailsResult
import com.popalay.cardme.domain.usecase.CardDetailsUseCase
import com.popalay.cardme.domain.usecase.GetCardDetailsAction
import com.popalay.cardme.domain.usecase.GetHolderNamesUseCase
import com.popalay.cardme.domain.usecase.Result
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundUseCase
import com.popalay.cardme.utils.extensions.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject
import javax.inject.Named

class AddCardViewModel @Inject constructor(
        @Named(AddCardActivity.KEY_CARD_NUMBER) cardNumber: String,
        private val router: CustomRouter,
        private val cardDetailsUseCase: CardDetailsUseCase,
        private val shouldShowCardBackgroundUseCase: ShouldShowCardBackgroundUseCase,
        private val getHolderNamesUseCase: GetHolderNamesUseCase
) : MviViewModel<AddCardViewState, AddCardIntent>() {

    private val intentFilter
        get() = ObservableTransformer<AddCardIntent, AddCardIntent> {
            it.publish {
                Observable.merge<AddCardIntent>(
                        it.ofType(AddCardIntent.InitialIntent::class.java).take(1),
                        it.notOfType(AddCardIntent.InitialIntent::class.java)
                )
            }
        }

    private val actions
        get() = ObservableTransformer<Action, Result> {
            it.publish {
                Observable.merge<Result>(
                        it.ofType(GetCardDetailsAction::class.java).compose(cardDetailsUseCase),
                        it.ofType(GetCardDetailsAction::class.java).compose(cardDetailsUseCase)
                )
            }
        }

    init {
        /*Observables.combineLatest(
                holderName.observe().doOnNext { card.set(card.execute()?.copy(holderName = it.clean())) },
                title.observe().doOnNext { card.set(card.execute()?.copy(title = it.clean())) })
                .switchMapSingle { cardInteractor.hasAllData(card.execute(), it.first) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(canSaveState::accept)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        acceptClickListener
                .applyThrottling()
                .filter { it && canSaveState.value }
                .map { card.execute() }
                .switchMapSingle { cardInteractor.save(it).toSingleDefault(true) }
                .doOnNext { router.exit() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        editorActionListener
                .applyThrottling()
                .filter { it == EditorInfo.IME_ACTION_`DONE }
                .doOnNext { acceptClickListener.accept(true) }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)*/
    }

    override fun reduce(oldState: AddCardViewState, result: Result): AddCardViewState = when (result) {
        is CardDetailsResult -> when (result) {
            is CardDetailsResult.Success -> oldState.copy(card = result.card)
            is CardDetailsResult.Failure -> oldState.copy(error = result.throwable)
            is CardDetailsResult.Idle -> oldState
        }
        else -> oldState
    }

    override fun actionFromIntent(intent: AddCardIntent): Action = when (intent) {
        is AddCardIntent.InitialIntent -> GetCardDetailsAction(intent.number)
        is AddCardIntent.NameChangedIntent -> TODO()
        is AddCardIntent.TitleChangedIntent -> TODO()
        is AddCardIntent.AcceptIntent -> TODO()
    }

    override fun compose(): Observable<AddCardViewState> = intentsSubject
            .compose(intentFilter)
            .map(this::actionFromIntent)
            .compose(actions)
            .scan(AddCardViewState.idle(), ::reduce)
            .replay(1)
            .autoConnect(0)
}

sealed class AddCardIntent : Intent {
    data class InitialIntent(val number: String) : AddCardIntent()
    data class NameChangedIntent(val name: String) : AddCardIntent()
    data class TitleChangedIntent(val title: String) : AddCardIntent()
    object AcceptIntent : AddCardIntent()
}

data class AddCardViewState(
        val card: Card,
        val showBackground: Boolean,
        val holderNames: List<String>,
        val canSave: Boolean,
        val error: Throwable?
) : ViewState {
    companion object {
        fun idle() = AddCardViewState(
                card = Card(),
                showBackground = false,
                holderNames = listOf(),
                canSave = false,
                error = null
        )
    }
}