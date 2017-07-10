package com.popalay.cardme.presentation.screens.splash

import com.arellomobile.mvp.presenter.InjectPresenter
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.screens.home.HomeActivity

class SplashActivity : BaseActivity(), SplashView {

    @InjectPresenter lateinit var presenter: SplashPresenter

    override fun openHome() {
        startActivity(HomeActivity.getIntent(this))
        finish()
    }
}
