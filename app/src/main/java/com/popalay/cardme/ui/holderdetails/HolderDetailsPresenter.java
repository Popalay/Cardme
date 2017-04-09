package com.popalay.cardme.ui.holderdetails;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.cards.CardsInteractor;
import com.popalay.cardme.business.debts.DebtsInteractor;
import com.popalay.cardme.business.holders.HoldersInteractor;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.ui.base.BasePresenter;

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
        cardsInteractor.incCardUsage(card)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getViewState().shareCardNumber(card.getNumber()), this::handleBaseError);
    }
}
