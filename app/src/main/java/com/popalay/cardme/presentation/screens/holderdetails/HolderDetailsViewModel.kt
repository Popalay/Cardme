package com.popalay.cardme.presentation.screens.holderdetails

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableList
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.App
import com.popalay.cardme.business.cards.CardInteractor
import com.popalay.cardme.business.debts.DebtsInteractor
import com.popalay.cardme.business.holders.HolderInteractor
import com.popalay.cardme.business.settings.SettingsInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.presentation.base.setTo
import com.stepango.rxdatabindings.ObservableString
import com.stepango.rxdatabindings.setTo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class HolderDetailsViewModel(application: Application, holderId: String) : AndroidViewModel(application) {

    @Inject lateinit var cardInteractor: CardInteractor
    @Inject lateinit var holderInteractor: HolderInteractor
    @Inject lateinit var debtsInteractor: DebtsInteractor
    @Inject lateinit var settingsInteractor: SettingsInteractor

    val debts: ObservableList<Debt> = ObservableArrayList()
    val cards: ObservableList<Card> = ObservableArrayList()
    val holderName = ObservableString()
    val showImage = ObservableBoolean()

    private val subscriptions = CompositeDisposable()

    val cardClickPublisher: PublishRelay<Card> = PublishRelay.create<Card>()

    init {
        App.appComponent().inject(this)

        cardInteractor.getCardsByHolder(holderId)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(cards)
                .subscribeBy()
                .addTo(subscriptions)

        debtsInteractor.getDebtsByHolder(holderId)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(debts)
                .subscribeBy()
                .addTo(subscriptions)

        holderInteractor.getHolderName(holderId)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holderName)
                .subscribeBy()
                .addTo(subscriptions)

        settingsInteractor.listenShowCardsBackground()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(showImage)
                .subscribeBy()
                .addTo(subscriptions)
    }

    fun doOnShareCard(): Observable<String> {
        return cardClickPublisher.map({ it.number })
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}
