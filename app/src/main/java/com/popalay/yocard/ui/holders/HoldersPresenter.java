package com.popalay.yocard.ui.holders;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.App;
import com.popalay.yocard.business.holders.HoldersInteractor;
import com.popalay.yocard.data.models.Holder;
import com.popalay.yocard.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class HoldersPresenter extends BasePresenter<HoldersView> {

    @Inject HoldersInteractor holdersInteractor;

    public HoldersPresenter() {
        App.appComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        holdersInteractor.getHolders()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setHolders, this::handleBaseError);
    }

    public void onHolderClick(Holder holder) {
        getViewState().openHolderCards(holder);
    }
}
