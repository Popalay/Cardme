package com.popalay.cardme.presentation.screens.holderdetails;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.jakewharton.rxrelay.PublishRelay;
import com.popalay.cardme.App;
import com.popalay.cardme.business.cards.CardInteractor;
import com.popalay.cardme.business.debts.DebtsInteractor;
import com.popalay.cardme.business.holders.HolderInteractor;
import com.popalay.cardme.business.settings.SettingsInteractor;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.data.models.Settings;
import com.popalay.cardme.utils.BindingUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class HolderDetailsViewModel extends AndroidViewModel {

    @Inject CardInteractor cardInteractor;
    @Inject HolderInteractor holderInteractor;
    @Inject DebtsInteractor debtsInteractor;
    @Inject SettingsInteractor settingsInteractor;

    public final ObservableList<Debt> debts = new ObservableArrayList<>();
    public final ObservableList<Card> cards = new ObservableArrayList<>();
    public final ObservableField<String> holderName = new ObservableField<>();
    public final ObservableBoolean showImage = new ObservableBoolean();

    private final CompositeSubscription subscriptions = new CompositeSubscription();

    public final PublishRelay<Card> cardClickPublisher = PublishRelay.create();

    public HolderDetailsViewModel(Application application, long holderId) {
        super(application);
        App.appComponent().inject(this);

        subscriptions.add(cardInteractor.getCardsByHolder(holderId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setCards, Throwable::printStackTrace));

        subscriptions.add(debtsInteractor.getDebtsByHolder(holderId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setDebts, Throwable::printStackTrace));

        subscriptions.add(holderInteractor.getHolderName(holderId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(holderName::set, Throwable::printStackTrace));

        subscriptions.add(settingsInteractor.listenSettings()
                .map(Settings::isCardBackground)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(showImage::set, Throwable::printStackTrace));
    }

    public Observable<String> doOnShareCard() {
        return cardClickPublisher.map(Card::getNumber);
    }

    @Override protected void onCleared() {
        super.onCleared();
        subscriptions.clear();
    }

    private void setDebts(List<Debt> items) {
        BindingUtils.setItems(debts, items);
    }

    private void setCards(List<Card> items) {
        BindingUtils.setItems(cards, items);
    }
}
