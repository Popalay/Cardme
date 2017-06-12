package com.popalay.cardme.ui.screens.holderdetails;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.cards.CardInteractor;
import com.popalay.cardme.business.debts.DebtsInteractor;
import com.popalay.cardme.business.holders.HolderInteractor;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class HolderDetailsPresenter extends BasePresenter<HolderDetailsView> {

    @Inject CardInteractor mCardInteractor;
    @Inject HolderInteractor mHolderInteractor;
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

        mCardInteractor.getCardsByHolder(holderId)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setCards, this::handleBaseError);

        debtsInteractor.getDebtsByHolder(holderId)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setDebts, this::handleBaseError);

        mHolderInteractor.getHolderName(holderId)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setHolderName, this::handleBaseError);
    }

    public void onCardClick(Card card) {
        getViewState().shareCardNumber(card.getNumber());
    }
}
