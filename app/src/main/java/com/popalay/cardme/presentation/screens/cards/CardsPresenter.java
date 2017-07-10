package com.popalay.cardme.presentation.screens.cards;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.App;
import com.popalay.cardme.business.cards.CardInteractor;
import com.popalay.cardme.business.settings.SettingsInteractor;
import com.popalay.cardme.data.AddCardEvent;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.presentation.ScreensKt;
import com.popalay.cardme.presentation.base.navigation.NavigationExtrasHolder;
import com.popalay.cardme.presentation.screens.removablelistitem.RemovableListItemPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.card.payment.CreditCard;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class CardsPresenter extends RemovableListItemPresenter<Card, CardsView> {

    @Inject Router router;
    @Inject CardInteractor cardInteractor;
    @Inject SettingsInteractor settingsInteractor;
    @Inject NavigationExtrasHolder navigationExtras;

    private final CardsViewModel viewModel;

    public CardsPresenter() {
        App.getAppComponent().inject(this);
        EventBus.getDefault().register(this);
        viewModel = new CardsViewModel();
        getViewState().setViewModel(viewModel);

        addDisposable(cardInteractor.getCards()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel::setCards, this::handleBaseError));

        addDisposable(settingsInteractor.listenShowCardsBackground()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel::setShowImage, this::handleBaseError));
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
        router.navigateTo(ScreensKt.SCREEN_ADD_CARD, card);
    }

    public void onCardClick(Card card) {
        getViewState().shareCardNumber(card.getNumber());
    }

    public void onItemDragged(List<Card> items, int from, int to) {
        Collections.swap(items, from, to);
        viewModel.setCards(items);
    }

    public void onItemDropped(List<Card> items) {
        addDisposable(cardInteractor.updateCards(items)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, this::handleBaseError));
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
