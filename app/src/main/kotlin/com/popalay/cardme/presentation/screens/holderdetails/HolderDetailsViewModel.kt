package com.popalay.cardme.presentation.screens.holderdetails

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.business.holders.HolderInteractor
import com.popalay.cardme.business.settings.SettingsInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.data.models.Holder
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.recycler.DiffObservableList
import com.stepango.rxdatabindings.ObservableString
import com.stepango.rxdatabindings.setTo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import javax.inject.Named

class HolderDetailsViewModel @Inject constructor(
        @Named(HolderDetailsActivity.KEY_HOLDER_DETAILS) holderName: String,
        holderInteractor: HolderInteractor,
        settingsInteractor: SettingsInteractor
) : BaseViewModel(), HolderDetailsViewModelFacade {

    val holder = ObservableField<Holder>()

    val debts = DiffObservableList<Debt>()
    val cards = DiffObservableList<Card>()
    val holderName = ObservableString(holderName)
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
    }

    override fun doOnShareCard(): Observable<String> = cardClickPublisher
            .applyThrottling()
            .map { it.number }

}

interface HolderDetailsViewModelFacade {

    fun doOnShareCard(): Observable<String>

}
