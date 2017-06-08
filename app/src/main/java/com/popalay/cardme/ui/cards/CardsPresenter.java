package com.popalay.cardme.ui.cards;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.cards.CardInteractor;
import com.popalay.cardme.business.settings.SettingsInteractor;
import com.popalay.cardme.data.events.AddCardEvent;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Settings;
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

    @Inject CardInteractor cardInteractor;
    @Inject SettingsInteractor settingsInteractor;

    private final CardsViewModel viewModel;

    public CardsPresenter() {
        App.appComponent().inject(this);
        EventBus.getDefault().register(this);
        viewModel = new CardsViewModel();
        getViewState().setViewModel(viewModel);

        cardInteractor.getCards()
                .compose(bindToLifecycle())
                .doOnNext(viewModel::setCards)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel::setCards, this::handleBaseError);

        settingsInteractor.listenSettings()
                .compose(bindToLifecycle())
                .map(Settings::isCardBackground)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(show -> viewModel.setShowImage(show), this::handleBaseError);
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
        Collections.swap(items, from, to);
        viewModel.setCards(items);
    }

    public void onItemDropped(List<Card> items) {
        cardInteractor.updateCards(items)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError);
    }

    @Override
    protected Completable removeItem(Card item) {
        return cardInteractor.removeCard(item);
    }

    @Override
    protected Completable saveItem(Card item) {
        return cardInteractor.save(item);
    }
}
