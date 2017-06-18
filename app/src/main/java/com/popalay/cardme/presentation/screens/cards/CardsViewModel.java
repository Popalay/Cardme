package com.popalay.cardme.presentation.screens.cards;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.utils.BindingObservable;

import java.util.List;

import rx.Observable;

public class CardsViewModel {

    public final ObservableField<List<Card>> cards = new ObservableField<>();
    public final ObservableBoolean showImage = new ObservableBoolean();

    public void setShowImage(Boolean show) {
        showImage.set(show);
        cards.notifyChange();
    }

    public void setCards(List<Card> items) {
        cards.set(items);
        cards.notifyChange();
    }

    public Observable<List<Card>> getCardsObservable() {
        return BindingObservable.create(cards);
    }

    public Observable<Boolean> getShowImageObservable() {
        return BindingObservable.create(showImage);
    }

    public Card get(int position) {
        return cards.get().get(position);
    }

}
