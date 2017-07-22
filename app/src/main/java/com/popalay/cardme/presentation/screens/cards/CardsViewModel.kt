package com.popalay.cardme.presentation.screens.cards

import android.databinding.ObservableBoolean
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.business.cards.CardInteractor
import com.popalay.cardme.business.settings.SettingsInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.screens.SCREEN_ADD_CARD
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
        private val settingsInteractor: SettingsInteractor
) : BaseViewModel(), CardsViewModelFacade {

    val cards = DiffObservableList<Card>()
    val showImage = ObservableBoolean()

    val cardClickPublisher: PublishRelay<Card> = PublishRelay.create<Card>()
    val addCardClickPublisher: PublishRelay<Boolean> = PublishRelay.create<Boolean>()

    val onSwiped: PublishRelay<Int> = PublishRelay.create()
    val onDragged: PublishRelay<Pair<Int, Int>> = PublishRelay.create()
    val onDropped: PublishRelay<Boolean> = PublishRelay.create()
    val onUndoSwipe: PublishRelay<Boolean> = PublishRelay.create()

    init {
        cardInteractor.getCards()
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
                .subscribe { router.navigateToForResult(SCREEN_SCAN_CARD, requestCode = CardsFragment.SCAN_REQUEST_CODE) }
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
                .flatMapSingle { cardInteractor.remove(it).toSingle { it } }
                .switchMap { card -> onUndoSwipe.filter { it }.map { card } }
                .flatMapCompletable(cardInteractor::restore)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy()
                .addTo(disposables)
    }

    override fun onCardScanned(creditCard: CreditCard) {
        router.navigateTo(SCREEN_ADD_CARD, creditCard)
    }

    override fun doOnShareCard(): Observable<String> = cardClickPublisher
            .applyThrottling()
            .map { it.number }
}
