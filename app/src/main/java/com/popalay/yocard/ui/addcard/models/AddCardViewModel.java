package com.popalay.yocard.ui.addcard.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.popalay.yocard.BR;
import com.popalay.yocard.data.models.Card;

public class AddCardViewModel extends BaseObservable {

    private Card card;

    public AddCardViewModel(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Bindable
    public String getHolderName() {
        return card.getHolder().getName();
    }

    public void setHolderName(String holderName) {
        card.getHolder().setName(holderName);
        notifyPropertyChanged(BR.holderName);
    }
}
