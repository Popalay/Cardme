package com.popalay.cardme.screens.addcard

import com.popalay.cardme.base.BaseViewModel
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.domain.interactor.CardInteractor
import com.popalay.cardme.domain.interactor.HolderInteractor
import com.popalay.cardme.domain.interactor.SettingsInteractor
import com.popalay.cardme.domain.model.Card
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
        cardInteractor: CardInteractor,
        holderInteractor: HolderInteractor,
        settingsInteractor: SettingsInteractor
) : BaseViewModelNew<AddCardState, AddCardAction, AddCardSignal>() {

    init {

        disposables += cardInteractor.get(cardNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { dispatch(AddCardAction.CardResultSuccess(it)) }
                .subscribeBy(this::handleBaseError)

        disposables += settingsInteractor.listenShowCardsBackground()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { dispatch(AddCardAction.CardBackgroundResultSuccess(it)) }
                .subscribeBy(this::handleBaseError)

        disposables += holderInteractor.getNames()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { dispatch(AddCardAction.HolderNamesResultSuccess(it)) }
                .subscribeBy(this::handleBaseError)

        /*Observables.combineLatest(
                holderName.observe().doOnNext { card.set(card.get()?.copy(holderName = it.clean())) },
                title.observe().doOnNext { card.set(card.get()?.copy(title = it.clean())) })
                .switchMapSingle { cardInteractor.hasAllData(card.get(), it.first) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(canSaveState::accept)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        acceptClickListener
                .applyThrottling()
                .filter { it && canSaveState.value }
                .map { card.get() }
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

    override fun reduce(oldState: AddCardState?, action: AddCardAction): AddCardState {
        val state = oldState ?: AddCardState()
        return when (action) {
            is AddCardAction.CardResultSuccess -> state.copy(card = action.card)
            is AddCardAction.HolderNamesResultSuccess -> state.copy(holderNames = action.holderNames)
            is AddCardAction.CardBackgroundResultSuccess -> state.copy(showBackground = action.showCardBackground)
            is AddCardAction.CardValidationSuccess -> state.copy(canSave = action.valid)
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

sealed class AddCardAction : Action {
    data class CardResultSuccess(val card: Card) : AddCardAction()
    data class HolderNamesResultSuccess(val holderNames: List<String>) : AddCardAction()
    data class CardBackgroundResultSuccess(val showCardBackground: Boolean) : AddCardAction()
    data class CardValidationSuccess(val valid: Boolean) : AddCardAction()
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

interface Action

interface State

interface Signal

interface Dispatcher<in A : Action> {
    fun dispatch(action: A)
}

interface Handler<in SI : Signal> {
    fun handle(signal: SI)
}

interface Reducer<S : State, in A : Action> {
    fun reduce(oldState: S?, action: A): S
}

interface StateProvider<S : State> {
    fun newState(): Observable<S>
}

abstract class BaseViewModelNew<S : State, in A : Action, in SI : Signal>
    : BaseViewModel(), Dispatcher<A>, Reducer<S, A>, StateProvider<S>, Handler<SI> {

    private val stateSubject: Subject<S> = BehaviorSubject.create<S>()

    override fun dispatch(action: A) {
        reduce(stateSubject.blockingLast(null), action).also(stateSubject::onNext)
    }

    override fun newState(): Observable<S> = stateSubject
}