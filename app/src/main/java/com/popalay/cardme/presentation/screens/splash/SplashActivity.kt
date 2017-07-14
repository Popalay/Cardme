package com.popalay.cardme.presentation.screens.splash

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.popalay.cardme.presentation.base.BaseActivity
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)
    }
}
