package com.popalay.yocard.ui.cards;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.ui.base.BasePresenter;
import com.popalay.yocard.data.models.Card;

import io.card.payment.CreditCard;

@InjectViewState
public class CardsPresenter extends BasePresenter<CardsView> {

    public void onAddButtonClick() {
        getViewState().startCardScanning();
    }

    public void onCardScanned(CreditCard card) {
        getViewState().addCardDetails(card);
    }
}
