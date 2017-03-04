package com.popalay.yocard.ui.cards;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.App;
import com.popalay.yocard.R;
import com.popalay.yocard.business.cards.CardsInteractor;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.ui.base.BasePresenter;

import javax.inject.Inject;

import io.card.payment.CreditCard;
import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class CardsPresenter extends BasePresenter<CardsView> {

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

    public void onCardSwiped(Card card, int position) {
        getViewState().removeCard(position);
        getViewState().showRemoveUndoAction(card, position);
    }

    public void onRemoveUndoActionDismissed(Card card, int position) {
        cardsInteractor.removeCard(card)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError);
    }

    public void onRemoveUndo(Card card, int position) {
        getViewState().resetCard(card, position);
    }
}
