package com.popalay.cardme.presentation.screens.holders

import android.databinding.ObservableArrayList
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.App
import com.popalay.cardme.business.holders.HolderInteractor
import com.popalay.cardme.data.models.Holder
import com.popalay.cardme.presentation.SCREEN_HOLDER_DETAILS
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.navigation.NavigationExtrasHolder
import com.popalay.cardme.presentation.base.setTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class HoldersViewModel : BaseViewModel() {

    @Inject lateinit var router: Router
    @Inject lateinit var holderInteractor: HolderInteractor
    @Inject lateinit var navigationExtras: NavigationExtrasHolder

    val holders = ObservableArrayList<Holder>()
    val holderClickListener: PublishRelay<Holder> = PublishRelay.create<Holder>()

    init {
        App.appComponent.inject(this)

        holderInteractor.getHolders()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holders)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        holderClickListener
                .doOnNext {
                    router.navigateTo(SCREEN_HOLDER_DETAILS, it)
                }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)
    }

}
