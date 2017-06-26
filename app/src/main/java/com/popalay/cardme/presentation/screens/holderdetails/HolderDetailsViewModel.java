package com.popalay.cardme.presentation.screens.holderdetails;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.presentation.base.BaseViewModel;
import com.popalay.cardme.utils.BindingObservable;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class HolderDetailsViewModel extends BaseViewModel {

    public final ObservableField<List<Debt>> debts = new ObservableField<>();
    public final ObservableList<Card> cards = new ObservableArrayList<>();
    public final ObservableField<String> holderName = new ObservableField<>();
    public final ObservableBoolean showImage = new ObservableBoolean();

    public final Observable<List<Debt>> debtsObservable = BindingObservable.create(debts);

    public void setShowImage(boolean show) {
        showImage.set(show);
    }

    public void setHolderName(String name) {
        holderName.set(name);
    }

    public void setDebts(List<Debt> items) {
        debts.set(items);
    }

    public void setCards(List<Card> items) {
        final List<Card> newList = new ArrayList<>(items);
        cards.clear();
        cards.addAll(newList);
    }
}
