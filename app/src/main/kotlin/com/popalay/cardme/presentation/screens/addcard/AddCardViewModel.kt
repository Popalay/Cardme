package com.popalay.cardme.presentation.screens.addcard

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.view.inputmethod.EditorInfo
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.business.cards.CardInteractor
import com.popalay.cardme.business.holders.HolderInteractor
import com.popalay.cardme.business.settings.SettingsInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.clean
import com.popalay.cardme.utils.extensions.setTo
import com.stepango.rxdatabindings.ObservableString
import com.stepango.rxdatabindings.observe
import com.stepango.rxdatabindings.setTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import javax.inject.Named

class AddCardViewModel @Inject constructor(
        private val router: CustomRouter,
        cardInteractor: CardInteractor,
        @Named(AddCardActivity.KEY_CARD_NUMBER) cardNumber: String,
        holderInteractor: HolderInteractor,
        settingsInteractor: SettingsInteractor
) : BaseViewModel() {

    val holderName = ObservableString()
    val holderNames: ObservableList<String> = ObservableArrayList<String>()
    val title = ObservableString()
    val canSave = ObservableBoolean()
    val showImage = ObservableBoolean()
    val card = ObservableField<Card>()

    val acceptClickListener: PublishRelay<Boolean> = PublishRelay.create<Boolean>()
    val editorActionListener: PublishRelay<Int> = PublishRelay.create<Int>()

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

        holderInteractor.getHolderNames()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holderNames)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        holderName.observe()
                .doOnNext { card.get()?.holder?.name = it.clean() }
                .switchMapSingle { cardInteractor.hasAllData(card.get()) }
                .setTo(canSave)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        title.observe()
                .doOnNext { card.get()?.title = it.clean() }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        acceptClickListener
                .applyThrottling()
                .filter { it }
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
                .addTo(disposables)
    }

}
