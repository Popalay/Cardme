package com.popalay.cardme.presentation.screens.splash

import com.popalay.cardme.business.splash.SplashInteractor
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.screens.SCREEN_HOME
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        router: CustomRouter,
        splashInteractor: SplashInteractor
) : BaseViewModel() {

    init {
        splashInteractor.start()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { router.newRootScreen(SCREEN_HOME) }
                .subscribeBy(this::handleBaseError)
    }
}