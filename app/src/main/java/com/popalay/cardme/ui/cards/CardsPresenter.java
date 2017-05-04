package com.popalay.cardme.ui.cards;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.cards.CardInteractor;
import com.popalay.cardme.data.events.AddCardEvent;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.ui.removablelistitem.RemovableListItemPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.card.payment.CreditCard;
import rx.Completable;
import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class CardsPresenter extends RemovableListItemPresenter<Card, CardsView> {

    @Inject CardInteractor mCardInteractor;
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

        mCardInteractor.getCards()
                .compose(bindToLifecycle())
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
        getViewState().shareCardNumber(card.getNumber());
    }

    public void onItemDragged(List<Card> items, int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        getViewState().setItems(items);
    }

    public void onItemDropped(List<Card> items) {
        mCardInteractor.updateCardPositions(items)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError);
    }

    @Override
    protected Completable removeItem(Card item) {
        return mCardInteractor.removeCard(item);
    }

    @Override
    protected Completable saveItem(Card item) {
        return mCardInteractor.save(item);
    }
}
