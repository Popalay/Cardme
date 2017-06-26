package com.popalay.cardme.presentation.screens.cards;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.utils.BindingUtils;

import java.util.List;

public class CardsViewModel {

    public final ObservableList<Card> cards = new ObservableArrayList<>();
    public final ObservableBoolean showImage = new ObservableBoolean();

    public void setShowImage(boolean show) {
        showImage.set(show);
    }

    public void setCards(List<Card> items) {
        BindingUtils.setItems(cards, items);
    }

    public Card get(int position) {
        return cards.get(position);
    }

}
