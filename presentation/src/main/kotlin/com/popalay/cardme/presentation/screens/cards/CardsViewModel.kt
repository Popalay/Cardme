package com.popalay.cardme.presentation.screens.cards

import android.databinding.ObservableBoolean
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.DataTransformers
import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.AppException
import com.popalay.cardme.domain.ExceptionFactory
import com.popalay.cardme.domain.interactor.CardInteractor
import com.popalay.cardme.domain.interactor.SettingsInteractor
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.screens.SCREEN_ADD_CARD
import com.popalay.cardme.presentation.screens.SCREEN_CARD_DETAILS
import com.popalay.cardme.presentation.screens.SCREEN_SCAN_CARD
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.setTo
import com.popalay.cardme.utils.extensions.swap
import com.popalay.cardme.utils.recycler.DiffObservableList
import com.stepango.rxdatabindings.setTo
import io.card.payment.CreditCard
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class CardsViewModel @Inject constructor(
        private val router: CustomRouter,
        private val cardInteractor: CardInteractor,
        settingsInteractor: SettingsInteractor
) : BaseViewModel(), CardsViewModelFacade {

    val cards = DiffObservableList<Card>()
    val showImage = ObservableBoolean()

    val cardClickPublisher: PublishRelay<Card> = PublishRelay.create<Card>()
    val addCardClickPublisher: PublishRelay<Boolean> = PublishRelay.create<Boolean>()

    val onSwiped: PublishRelay<Int> = PublishRelay.create()
    val onDragged: PublishRelay<Pair<Int, Int>> = PublishRelay.create()
    val onDropped: PublishRelay<Boolean> = PublishRelay.create()
    val onUndoSwipe: PublishRelay<Boolean> = PublishRelay.create()

    val errorDialogState: BehaviorRelay<Boolean> = BehaviorRelay.create<Boolean>()

    private var lastScannedCard: Card? = null

    init {
        cardInteractor.getAll()
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(cards)
                .subscribeBy()
                .addTo(disposables)

        settingsInteractor.listenShowCardsBackground()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(showImage)
                .subscribeBy()
                .addTo(disposables)

        addCardClickPublisher
                .applyThrottling()
                .subscribe { router.navigateToForResult(SCREEN_SCAN_CARD, CardsFragment.SCAN_REQUEST_CODE) }
                .addTo(disposables)

        cardClickPublisher
                .applyThrottling()
                .subscribe { router.navigateTo(SCREEN_CARD_DETAILS, it.number) }
                .addTo(disposables)

        onDragged
                .map(cards::swap)
                .subscribeBy()
                .addTo(disposables)

        onDropped
                .map { cards }
                .flatMapCompletable(cardInteractor::update)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy()
                .addTo(disposables)

        onSwiped
                .map(cards::get)
                .flatMapSingle { cardInteractor.markAsTrash(it).toSingle { it } }
                .switchMap { card -> onUndoSwipe.filter { it }.map { card } }
                .flatMapCompletable(cardInteractor::restore)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy()
                .addTo(disposables)
    }

    override fun doOnShowCardExistsDialog(): Observable<Boolean> = errorDialogState.filter { it }

    override fun onShowCardExistsDialogDismiss() = errorDialogState.accept(false)

    override fun onWantToOverwrite() = navigateToAddCard()

    override fun onCardScanned(creditCard: CreditCard) {
        lastScannedCard = DataTransformers.transform(creditCard)
        cardInteractor.checkCardExist(lastScannedCard?.number)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(this::navigateToAddCard)
                .subscribeBy(this::handleLocalError)
                .addTo(disposables)
    }

    override fun getPositionOfCard(number: String) = cards.indexOfFirst { it.number == number }

    private fun navigateToAddCard() = router.navigateTo(SCREEN_ADD_CARD, lastScannedCard)

    private fun handleLocalError(throwable: Throwable) {
        handleBaseError(throwable)
        if (throwable !is AppException) return
        when (throwable.errorType) {
            ExceptionFactory.ErrorType.CARD_EXIST -> errorDialogState.accept(true)
            else -> {
            }
        }
    }
}

interface CardsViewModelFacade {

    fun onCardScanned(creditCard: CreditCard)
    fun onShowCardExistsDialogDismiss()
    fun onWantToOverwrite()
    fun doOnShowCardExistsDialog(): Observable<Boolean>
    fun getPositionOfCard(number: String): Int
}
