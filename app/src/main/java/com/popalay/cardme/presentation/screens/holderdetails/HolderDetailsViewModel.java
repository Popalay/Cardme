package com.popalay.cardme.presentation.screens.holderdetails;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.presentation.base.BaseViewModel;
import com.popalay.cardme.utils.BindingObservable;

import java.util.List;

import rx.Observable;

public class HolderDetailsViewModel extends BaseViewModel {

    public final ObservableField<List<Debt>> debts = new ObservableField<>();
    public final ObservableField<List<Card>> cards = new ObservableField<>();
    public final ObservableField<String> holderName = new ObservableField<>();
    public final ObservableBoolean showImage = new ObservableBoolean();

    public final Observable<List<Card>> cardsObservable = BindingObservable.create(cards);
    public final Observable<List<Debt>> debtsObservable = BindingObservable.create(debts);
    public final Observable<Boolean> showImageObservable = BindingObservable.create(showImage);

    public void setShowImage(boolean show) {
        showImage.set(show);
        //cards.notifyChange();
    }

    public void setHolderName(String name) {
        holderName.set(name);
    }

    public void setDebts(List<Debt> items) {
        debts.set(items);
        //debts.notifyChange();
    }

    public void setCards(List<Card> items) {
        cards.set(items);
        //cards.notifyChange();
    }
}
