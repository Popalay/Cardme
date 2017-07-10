package com.popalay.cardme.presentation.screens.cards

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.business.cards.CardInteractor
import com.popalay.cardme.business.settings.SettingsInteractor
import com.popalay.cardme.data.AddCardEvent
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.applyThrottling
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.base.setTo
import com.popalay.cardme.presentation.screens.SCREEN_SCAN_CARD
import com.stepango.rxdatabindings.setTo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class CardsViewModel @Inject constructor(
        private val router: CustomRouter,
        private val cardInteractor: CardInteractor,
        private val settingsInteractor: SettingsInteractor
) : BaseViewModel(), CardsViewModelFacade {

    val cards = ObservableArrayList<Card>()
    val showImage = ObservableBoolean()

    val cardClickPublisher: PublishRelay<Card> = PublishRelay.create<Card>()
    val addCardClickPublisher: PublishRelay<Boolean> = PublishRelay.create<Boolean>()

    init {
        EventBus.getDefault().register(this)
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
                .subscribe{router.navigateToForResult(SCREEN_SCAN_CARD, requestCode = CardsFragment.SCAN_REQUEST_CODE)}
                .addTo(disposables)
    }

    override fun doOnShareCard(): Observable<String> = cardClickPublisher
            .applyThrottling()
            .map { it.number }

    @Subscribe(sticky = true)
    fun onAddCardShortCutEvent(event: AddCardEvent) {
        EventBus.getDefault().removeStickyEvent(event)
        router.navigateToForResult(SCREEN_SCAN_CARD, requestCode = CardsFragment.SCAN_REQUEST_CODE)
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
        super.onCleared()
    }
}
