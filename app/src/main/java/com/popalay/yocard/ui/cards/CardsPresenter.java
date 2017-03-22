package com.popalay.yocard.ui.cards;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.App;
import com.popalay.yocard.R;
import com.popalay.yocard.business.cards.CardsInteractor;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.ui.removablelistitem.RemovableListItemPresenter;

import javax.inject.Inject;

import io.card.payment.CreditCard;
import rx.Completable;
import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class CardsPresenter extends RemovableListItemPresenter<Card, CardsView> {

    @Inject CardsInteractor cardsInteractor;
    @Inject Context context;

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

    public void onCardClick(Card card) {
        cardsInteractor.copyCard(card)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getViewState().showMessage(context.getString(R.string.number_copied)),
                        this::handleBaseError);
    }

    @Override
    protected Completable removeItem(Card item) {
        return cardsInteractor.removeCard(item);
    }
}
