package com.popalay.cardme.presentation.screens.carddetails

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.popalay.cardme.business.cards.CardInteractor
import com.popalay.cardme.business.settings.SettingsInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.presentation.base.BaseViewModel
import com.stepango.rxdatabindings.setTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import javax.inject.Named

class CardDetailsViewModel @Inject constructor(
        @Named(CardDetailsActivity.KEY_CARD_NUMBER) cardNumber: String,
        cardInteractor: CardInteractor,
        settingsInteractor: SettingsInteractor
) : BaseViewModel() {

    val showImage = ObservableBoolean()
    val card = ObservableField<Card>()


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
    }
}