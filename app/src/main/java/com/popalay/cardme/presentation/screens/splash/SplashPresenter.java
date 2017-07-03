package com.popalay.cardme.presentation.screens.splash;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.splash.SplashInteractor;
import com.popalay.cardme.presentation.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {

    @Inject SplashInteractor splashInteractor;

    public SplashPresenter() {
        App.appComponent().inject(this);

        addDisposable(splashInteractor.start()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::openHome, this::handleBaseError));
    }
}
