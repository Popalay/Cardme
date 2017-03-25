package com.popalay.yocard.ui.holders;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.App;
import com.popalay.yocard.business.holders.HoldersInteractor;
import com.popalay.yocard.data.events.FavoriteHolderEvent;
import com.popalay.yocard.data.models.Holder;
import com.popalay.yocard.ui.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class HoldersPresenter extends BasePresenter<HoldersView> {

    @Inject HoldersInteractor holdersInteractor;

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

        holdersInteractor.getHolders()
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
