package com.popalay.yocard.ui.cards;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.App;
import com.popalay.yocard.business.cards.CardsInteractor;
import com.popalay.yocard.ui.base.BasePresenter;

import javax.inject.Inject;

import io.card.payment.CreditCard;
import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class CardsPresenter extends BasePresenter<CardsView> {

    @Inject CardsInteractor cardsInteractor;

    public CardsPresenter() {
        App.appComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        cardsInteractor.getCards()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setCards, this::handleBaseError);
    }

    public void onAddClick() {
        getViewState().startCardScanning();
    }

    public void onCardScanned(CreditCard card) {
        getViewState().addCardDetails(card);
    }
}
