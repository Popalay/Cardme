package com.popalay.cardme.presentation.screens.holders;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.holders.HolderInteractor;
import com.popalay.cardme.data.events.FavoriteHolderEvent;
import com.popalay.cardme.data.models.Holder;
import com.popalay.cardme.presentation.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class HoldersPresenter extends BasePresenter<HoldersView> {

    @Inject HolderInteractor mHolderInteractor;

    private boolean openFavoriteHolder;

    public HoldersPresenter() {
        App.appComponent().inject(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onFavoriteHolderEvent(FavoriteHolderEvent event) {
        EventBus.getDefault().removeStickyEvent(FavoriteHolderEvent.class);
        openFavoriteHolder = true;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        mHolderInteractor.getHolders()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(holders -> {
                    if (!holders.isEmpty() && openFavoriteHolder) {
                        onHolderClick(holders.get(0));
                        openFavoriteHolder = false;
                    }
                    getViewState().setHolders(holders);
                }, this::handleBaseError);
    }

    public void onHolderClick(Holder holder) {
        getViewState().openHolderDetails(holder);
    }
}
