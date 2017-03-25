package com.popalay.yocard.ui.cards;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.App;
import com.popalay.yocard.business.cards.CardsInteractor;
import com.popalay.yocard.data.events.AddCardEvent;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.ui.removablelistitem.RemovableListItemPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onAddCardShortCutEvent(AddCardEvent event) {
        EventBus.getDefault().removeStickyEvent(AddCardEvent.class);
        onAddClick();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        cardsInteractor.getCards()
                .compose(bindToLifecycle())
                .doOnNext(this::setItems)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setItems, this::handleBaseError);
    }

    public void onAddClick() {
        getViewState().startCardScanning();
    }

    public void onCardScanned(CreditCard card) {
        getViewState().addCardDetails(card);
    }

    public void onCardClick(Card card) {
        cardsInteractor.shareCard(card)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError);
    }

    @Override
    protected Completable removeItem(Card item) {
        return cardsInteractor.removeCard(item);
    }
}
