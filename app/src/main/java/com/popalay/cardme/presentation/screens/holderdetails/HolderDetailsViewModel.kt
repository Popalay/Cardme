package com.popalay.cardme.presentation.screens.holderdetails

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.jakewharton.rxrelay.PublishRelay
import com.popalay.cardme.App
import com.popalay.cardme.business.cards.CardInteractor
import com.popalay.cardme.business.debts.DebtsInteractor
import com.popalay.cardme.business.holders.HolderInteractor
import com.popalay.cardme.business.settings.SettingsInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.presentation.base.bindSubscribe
import com.popalay.cardme.presentation.base.setTo
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class HolderDetailsViewModel(application: Application, holderId: Long) : AndroidViewModel(application) {

    @Inject lateinit var cardInteractor: CardInteractor
    @Inject lateinit var holderInteractor: HolderInteractor
    @Inject lateinit var debtsInteractor: DebtsInteractor
    @Inject lateinit var settingsInteractor: SettingsInteractor

    val debts: ObservableList<Debt> = ObservableArrayList()
    val cards: ObservableList<Card> = ObservableArrayList()
    val holderName = ObservableField<String>()
    val showImage = ObservableBoolean()

    private val subscriptions = CompositeSubscription()

    val cardClickPublisher: PublishRelay<Card> = PublishRelay.create<Card>()

    init {
        App.appComponent().inject(this)

        cardInteractor.getCardsByHolder(holderId)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(cards)
                .bindSubscribe(subscriptions)

        debtsInteractor.getDebtsByHolder(holderId)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(debts)
                .bindSubscribe(subscriptions)

        holderInteractor.getHolderName(holderId)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holderName)
                .bindSubscribe(subscriptions)

        settingsInteractor.listenSettings()
                .map { it.isCardBackground }
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(showImage)
                .bindSubscribe(subscriptions)
    }

    fun doOnShareCard(): Observable<String> {
        return cardClickPublisher.map<String>({ it.getNumber() })
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}
