package com.popalay.cardme.presentation.screens.splash;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.popalay.cardme.presentation.base.BaseActivity;
import com.popalay.cardme.presentation.screens.home.HomeActivity;

public class SplashActivity extends BaseActivity implements SplashView {

    @InjectPresenter SplashPresenter presenter;

    @Override public void openHome() {
        startActivity(HomeActivity.Companion.getIntent(this));
        finish();
    }
}
