package com.popalay.cardme.screens.splash

import android.os.Bundle
import com.popalay.cardme.domain.interactor.SplashInteractor
import com.popalay.cardme.base.BaseActivity
import com.popalay.cardme.screens.home.HomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject lateinit var splashInteractor: SplashInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashInteractor.start()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    startActivity(HomeActivity.getIntent(this))
                    finish()
                }
                .subscribeBy()
    }
}
