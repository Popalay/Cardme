package com.popalay.cardme.presentation.screens.trash

import android.databinding.ObservableBoolean
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.domain.interactor.CardInteractor
import com.popalay.cardme.domain.interactor.SettingsInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.setTo
import com.popalay.cardme.utils.recycler.DiffObservableList
import com.stepango.rxdatabindings.setTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class TrashViewModel @Inject constructor(
        settingsInteractor: SettingsInteractor,
        private val cardInteractor: CardInteractor
) : BaseViewModel() {

    val cards = DiffObservableList<Card>()
    val showImage = ObservableBoolean()

    val onSwiped: PublishRelay<Int> = PublishRelay.create()
    val emptyTrashClick: PublishRelay<Boolean> = PublishRelay.create()

    init {
        cardInteractor.getAllTrashed()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(cards)
                .subscribeBy()
                .addTo(disposables)

        settingsInteractor.listenShowCardsBackground()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(showImage)
                .subscribeBy()
                .addTo(disposables)

        onSwiped
                .map(cards::get)
                .flatMapCompletable { cardInteractor.restore(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy()
                .addTo(disposables)

        emptyTrashClick
                .applyThrottling()
                .flatMapCompletable { cardInteractor.emptyTrash() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy()
                .addTo(disposables)
    }
}
