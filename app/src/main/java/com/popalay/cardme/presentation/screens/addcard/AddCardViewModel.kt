package com.popalay.cardme.presentation.screens.addcard

import android.app.Application
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.view.inputmethod.EditorInfo
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.App
import com.popalay.cardme.business.cards.CardInteractor
import com.popalay.cardme.business.exception.AppException
import com.popalay.cardme.business.holders.HolderInteractor
import com.popalay.cardme.business.settings.SettingsInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.applyThrottling
import com.popalay.cardme.presentation.base.setTo
import com.stepango.rxdatabindings.ObservableString
import com.stepango.rxdatabindings.observe
import com.stepango.rxdatabindings.setTo
import io.card.payment.CreditCard
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class AddCardViewModel(application: Application, creditCard: CreditCard) : BaseViewModel(application) {

    @Inject lateinit var cardInteractor: CardInteractor
    @Inject lateinit var holderInteractor: HolderInteractor
    @Inject lateinit var settingsInteractor: SettingsInteractor

    val holderName = ObservableString()
    val holderNames = ObservableArrayList<String>()
    val title = ObservableString()
    val canSave = ObservableBoolean()
    val showImage = ObservableBoolean()
    val card = ObservableField<Card>()

    val acceptClickListener: PublishRelay<Boolean> = PublishRelay.create<Boolean>()
    val editorActionListener: PublishRelay<Int> = PublishRelay.create<Int>()

    init {
        App.appComponent.inject(this)

        cardInteractor.transformCard(creditCard)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(card)
                .subscribeBy(this::handleLocalError)
                .addTo(disposables)

        settingsInteractor.listenShowCardsBackground()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(showImage)
                .subscribeBy(this::handleLocalError)
                .addTo(disposables)

        holderInteractor.getHolderNames()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holderNames)
                .subscribeBy(this::handleLocalError)
                .addTo(disposables)

        holderName.observe()
                .doOnNext { card.get()?.holder?.name = it.trim() }
                .doOnNext { canSave.set(!it.isNullOrBlank()) }
                .subscribeBy(onError = this::handleLocalError)
                .addTo(disposables)

        title.observe()
                .doOnNext { card.get()?.title = it.trim() }
                .subscribeBy(this::handleLocalError)
                .addTo(disposables)

        acceptClickListener.applyThrottling()
                .doOnNext { saveCard(card.get()) }
                .subscribeBy(this::handleLocalError)
                .addTo(disposables)

        editorActionListener.applyThrottling()
                .filter { it == EditorInfo.IME_ACTION_DONE }
                .doOnNext { acceptClickListener.accept(true) }
                .subscribeBy(this::handleLocalError)
                .addTo(disposables)
    }

    private fun saveCard(card: Card) = cardInteractor.save(card)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { router.exit() }
            .subscribeBy(this::handleLocalError)
            .addTo(disposables)

    private fun handleLocalError(throwable: Throwable) {
        handleBaseError(throwable)
        if (throwable !is AppException) return
        router.showError(throwable)
    }
}
