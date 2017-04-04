package com.popalay.yocard.ui.holderdetails;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.App;
import com.popalay.yocard.business.cards.CardsInteractor;
import com.popalay.yocard.business.debts.DebtsInteractor;
import com.popalay.yocard.business.holders.HoldersInteractor;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class HolderDetailsPresenter extends BasePresenter<HolderDetailsView> {

    @Inject CardsInteractor cardsInteractor;
    @Inject HoldersInteractor holdersInteractor;
    @Inject DebtsInteractor debtsInteractor;
    @Inject Context context;

    private long holderId;

    public HolderDetailsPresenter(long holderId) {
        this.holderId = holderId;
        App.appComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        cardsInteractor.getCardsByHolder(holderId)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setCards, this::handleBaseError);

        debtsInteractor.getDebtsByHolder(holderId)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setDebts, this::handleBaseError);

        holdersInteractor.getHolderName(holderId)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setHolderName, this::handleBaseError);
    }

    public void onCardClick(Card card) {
        cardsInteractor.shareCard(card)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError);
    }
}
