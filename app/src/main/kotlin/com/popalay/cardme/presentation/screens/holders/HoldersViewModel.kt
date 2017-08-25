package com.popalay.cardme.presentation.screens.holders

import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.business.interactor.HolderInteractor
import com.popalay.cardme.data.models.Holder
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.screens.SCREEN_HOLDER_DETAILS
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.setTo
import com.popalay.cardme.utils.recycler.DiffObservableList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class HoldersViewModel @Inject constructor(
        router: CustomRouter,
        holderInteractor: HolderInteractor
) : BaseViewModel() {

    val holders = DiffObservableList<Holder>()
    val holderClickListener: PublishRelay<Holder> = PublishRelay.create<Holder>()

    init {
        holderInteractor.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holders)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        holderClickListener
                .applyThrottling()
                .doOnNext { router.navigateTo(SCREEN_HOLDER_DETAILS, it.name) }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)
    }

}
