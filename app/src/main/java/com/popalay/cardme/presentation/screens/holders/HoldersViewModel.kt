package com.popalay.cardme.presentation.screens.holders

import android.databinding.ObservableArrayList
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.business.holders.HolderInteractor
import com.popalay.cardme.data.FavoriteHolderEvent
import com.popalay.cardme.data.models.Holder
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.base.setTo
import com.popalay.cardme.presentation.screens.SCREEN_HOLDER_DETAILS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class HoldersViewModel @Inject constructor(
        private val router: CustomRouter,
        private val holderInteractor: HolderInteractor
) : BaseViewModel() {

    val holders = ObservableArrayList<Holder>()
    val holderClickListener: PublishRelay<Holder> = PublishRelay.create<Holder>()

    init {
        EventBus.getDefault().register(this)
        holderInteractor.getHolders()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holders)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        holderClickListener
                .doOnNext { router.navigateTo(SCREEN_HOLDER_DETAILS, it.id) }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
        super.onCleared()
    }

    @Subscribe(sticky = true)
    fun onFavoriteHolderEvent(event: FavoriteHolderEvent) {
        EventBus.getDefault().removeStickyEvent(event)
        holderInteractor.getFavoriteHolder()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { router.navigateTo(SCREEN_HOLDER_DETAILS, it.id) }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)
    }

}
