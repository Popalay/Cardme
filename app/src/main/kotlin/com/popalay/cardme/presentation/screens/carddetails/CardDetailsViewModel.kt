package com.popalay.cardme.presentation.screens.carddetails

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.business.cards.CardInteractor
import com.popalay.cardme.business.settings.SettingsInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.screens.SCREEN_ADD_CARD
import com.popalay.cardme.utils.extensions.applyThrottling
import com.stepango.rxdatabindings.setTo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import javax.inject.Named

class CardDetailsViewModel @Inject constructor(
        private val router: CustomRouter,
        @Named(CardDetailsActivity.KEY_CARD_NUMBER) cardNumber: String,
        cardInteractor: CardInteractor,
        settingsInteractor: SettingsInteractor
) : BaseViewModel(), CardDetailsViewModelFacade {

    val showImage = ObservableBoolean()
    val card = ObservableField<Card>()

    val shareCardClick: PublishRelay<Boolean> = PublishRelay.create()
    val shareNfcCardClick: PublishRelay<Boolean> = PublishRelay.create()
    val editCardClick: PublishRelay<Boolean> = PublishRelay.create()

    init {
        cardInteractor.get(cardNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(card)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        settingsInteractor.listenShowCardsBackground()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(showImage)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        editCardClick
                .applyThrottling()
                .doOnNext { router.navigateTo(SCREEN_ADD_CARD, card.get()) }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)
    }

    override fun onShareCard(): Observable<String> = shareCardClick
            .applyThrottling()
            .map { card.get().number }

}

interface CardDetailsViewModelFacade {

    fun onShareCard(): Observable<String>
}