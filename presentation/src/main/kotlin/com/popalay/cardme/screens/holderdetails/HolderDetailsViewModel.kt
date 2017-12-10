package com.popalay.cardme.screens.holderdetails

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.base.BaseViewModel
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.domain.interactor.CardInteractor
import com.popalay.cardme.domain.interactor.DebtsInteractor
import com.popalay.cardme.domain.interactor.HolderInteractor
import com.popalay.cardme.domain.interactor.SettingsInteractor
import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.model.Debt
import com.popalay.cardme.domain.model.Holder
import com.popalay.cardme.screens.SCREEN_CARD_DETAILS
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.setTo
import com.popalay.cardme.utils.recycler.DiffObservableList
import com.stepango.rxdatabindings.setTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import javax.inject.Named

class HolderDetailsViewModel @Inject constructor(
        private val router: CustomRouter,
        @Named(HolderDetailsActivity.KEY_HOLDER_DETAILS) holderName: String,
        holderInteractor: HolderInteractor,
        cardInteractor: CardInteractor,
        debtsInteractor: DebtsInteractor,
        settingsInteractor: SettingsInteractor
) : BaseViewModel(), HolderDetailsViewModelFacade {

    var debts = DiffObservableList<Debt>()
    var cards = DiffObservableList<Card>()
    val showImage = ObservableBoolean()

    val cardClickPublisher: PublishRelay<Card> = PublishRelay.create<Card>()

    private val holder = ObservableField<Holder>()

    init {

        holderInteractor.get(holderName)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holder)
                .subscribeBy()
                .addTo(disposables)

        cardInteractor.getAllNotTrashedByHolder(holderName)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(cards)
                .subscribeBy()
                .addTo(disposables)

        debtsInteractor.getAllNotTrashedByHolder(holderName)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(debts)
                .subscribeBy()
                .addTo(disposables)

        settingsInteractor.listenShowCardsBackground()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(showImage)
                .subscribeBy()
                .addTo(disposables)

        cardClickPublisher
                .applyThrottling()
                .subscribeBy { router.navigateTo(SCREEN_CARD_DETAILS, it.number) }
                .addTo(disposables)
    }

    override fun getPositionOfCard(number: String) = cards.indexOfFirst { it.number == number }

}

interface HolderDetailsViewModelFacade {

    fun getPositionOfCard(number: String): Int

}
