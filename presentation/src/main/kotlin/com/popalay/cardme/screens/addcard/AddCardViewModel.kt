package com.popalay.cardme.screens.addcard

import com.popalay.cardme.base.BaseViewModel
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.usecase.Action
import com.popalay.cardme.domain.usecase.GetCardByNumberAction.CardResultSuccess
import com.popalay.cardme.domain.usecase.GetCardByNumberUseCase
import com.popalay.cardme.domain.usecase.GetHolderNamesAction.HolderNamesResultSuccess
import com.popalay.cardme.domain.usecase.GetHolderNamesUseCase
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundAction.ShowCardBackgroundSuccess
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundUseCase
import com.popalay.cardme.domain.usecase.ValidateCardAction.ValidateCardResult
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Named

class AddCardViewModel @Inject constructor(
        @Named(AddCardActivity.KEY_CARD_NUMBER) cardNumber: String,
        private val router: CustomRouter,
        getCardByNumberUseCase: GetCardByNumberUseCase,
        shouldShowCardBackgroundUseCase: ShouldShowCardBackgroundUseCase,
        getHolderNamesUseCase: GetHolderNamesUseCase
) : BaseViewModelNew<AddCardState, AddCardSignal>() {

    init {

        disposables += Flowable.merge(
                getCardByNumberUseCase.execute(cardNumber),
                shouldShowCardBackgroundUseCase.execute(),
                getHolderNamesUseCase.execute()
        )
                .doOnNext(::dispatch)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(this::handleBaseError)

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
                .filter { it == EditorInfo.IME_ACTION_DONE }
                .doOnNext { acceptClickListener.accept(true) }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)*/
    }

    override fun reduce(oldState: AddCardState?, action: Action): AddCardState {
        val state = oldState ?: AddCardState()
        return when (action) {
            is CardResultSuccess -> state.copy(card = action.card)
            is ShowCardBackgroundSuccess -> state.copy(showBackground = action.show)
            is HolderNamesResultSuccess -> state.copy(holderNames = action.names)
            is ValidateCardResult -> state.copy(canSave = action.valid)
            else -> state
        }
    }

    override fun handle(signal: AddCardSignal) {
        when (signal) {
            is AddCardSignal.NameChangedSignal -> TODO()
            is AddCardSignal.TitleChangedSignal -> TODO()
            is AddCardSignal.CardValidationSuccess -> TODO()
            is AddCardSignal.AcceptSignal -> TODO()
        }
    }
}

sealed class AddCardSignal : Signal {
    data class NameChangedSignal(val name: String) : AddCardSignal()
    data class TitleChangedSignal(val title: String) : AddCardSignal()
    data class CardValidationSuccess(val valid: Boolean) : AddCardSignal()
    object AcceptSignal : AddCardSignal()
}

data class AddCardState(
        val card: Card = Card(),
        val showBackground: Boolean = false,
        val holderNames: List<String> = listOf(),
        val canSave: Boolean = false
) : State

interface State

interface Signal

interface Dispatcher {
    fun dispatch(action: Action)
}

interface Handler<in SI : Signal> {
    fun handle(signal: SI)
}

interface Reducer<S : State> {
    fun reduce(oldState: S?, action: Action): S
}

interface StateProvider<S : State> {
    fun newState(): Observable<S>
}

abstract class BaseViewModelNew<S : State, in SI : Signal>
    : BaseViewModel(), Dispatcher, Reducer<S>, StateProvider<S>, Handler<SI> {

    private val stateSubject: Subject<S> = BehaviorSubject.create<S>()

    override fun dispatch(action: Action) {
        reduce(stateSubject.blockingLast(null), action).also(stateSubject::onNext)
    }

    override fun newState(): Observable<S> = stateSubject
}