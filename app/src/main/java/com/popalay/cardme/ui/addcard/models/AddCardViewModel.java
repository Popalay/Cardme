package com.popalay.cardme.ui.addcard.models;

import android.databinding.Observable;
import android.databinding.ObservableField;

import com.popalay.cardme.data.models.Card;

public class AddCardViewModel {

    public final ObservableField<String> holderName = new ObservableField<>();

    public final Card card;

    public AddCardViewModel(Card card) {
        this.card = card;

        holderName.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                card.getHolder().setName(holderName.get());
            }
        });
    }
}
