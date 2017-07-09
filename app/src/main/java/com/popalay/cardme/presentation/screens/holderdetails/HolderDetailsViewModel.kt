package com.popalay.cardme.presentation.screens.holderdetails

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.App
import com.popalay.cardme.business.cards.CardInteractor
import com.popalay.cardme.business.debts.DebtsInteractor
import com.popalay.cardme.business.holders.HolderInteractor
import com.popalay.cardme.business.settings.SettingsInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.setTo
import com.stepango.rxdatabindings.ObservableString
import com.stepango.rxdatabindings.setTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class HolderDetailsViewModel @Inject constructor() : BaseViewModel(), HolderDetailsViewModelFacade {

    val holderId: String = ""

    @Inject lateinit var cardInteractor: CardInteractor
    @Inject lateinit var holderInteractor: HolderInteractor
    @Inject lateinit var debtsInteractor: DebtsInteractor
    @Inject lateinit var settingsInteractor: SettingsInteractor

    val debts = ObservableArrayList<Debt>()
    val cards = ObservableArrayList<Card>()
    val holderNames = ObservableArrayList<String>()
    val holderName = ObservableString()
    val showImage = ObservableBoolean()

    val cardClickPublisher: PublishRelay<Card> = PublishRelay.create<Card>()

    init {
        App.appComponent.inject(this)

        cardInteractor.getCardsByHolder(holderId)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(cards)
                .subscribeBy()
                .addTo(disposables)

        debtsInteractor.getDebtsByHolder(holderId)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(debts)
                .subscribeBy()
                .addTo(disposables)

        holderInteractor.getHolderName(holderId)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holderName)
                .subscribeBy()
                .addTo(disposables)

        holderInteractor.getHolderNames()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holderNames)
                .subscribeBy()
                .addTo(disposables)

        settingsInteractor.listenShowCardsBackground()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(showImage)
                .subscribeBy()
                .addTo(disposables)
    }

    override fun doOnShareCard(body: (String) -> Unit): Disposable {
        return cardClickPublisher.map { it.number }.subscribe(body::invoke)
    }
}
