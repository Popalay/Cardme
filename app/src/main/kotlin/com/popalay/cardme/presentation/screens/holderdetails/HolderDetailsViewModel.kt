package com.popalay.cardme.presentation.screens.holderdetails

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.domain.interactor.HolderInteractor
import com.popalay.cardme.domain.interactor.SettingsInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.models.Holder
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.screens.SCREEN_CARD_DETAILS
import com.popalay.cardme.utils.extensions.applyThrottling
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
        settingsInteractor: SettingsInteractor
) : BaseViewModel(), HolderDetailsViewModelFacade {

    val holder = ObservableField<Holder>()
    val showImage = ObservableBoolean()

    val cardClickPublisher: PublishRelay<Card> = PublishRelay.create<Card>()

    init {

        holderInteractor.get(holderName)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holder)
                .subscribeBy()
                .addTo(disposables)

        settingsInteractor.listenShowCardsBackground()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(showImage)
                .subscribeBy()
                .addTo(disposables)

        cardClickPublisher
                .applyThrottling()
                .subscribe { router.navigateTo(SCREEN_CARD_DETAILS, it.number) }
                .addTo(disposables)
    }

    override fun getPositionOfCard(number: String) = holder.get().cards.indexOfFirst { it.number == number }

}

interface HolderDetailsViewModelFacade {

    fun getPositionOfCard(number: String): Int

}
