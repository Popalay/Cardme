package com.popalay.cardme.presentation.screens.holderdetails;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.presentation.base.BaseViewModel;
import com.popalay.cardme.utils.BindingUtils;

import java.util.List;

public class HolderDetailsViewModel extends BaseViewModel {

    public final ObservableList<Debt> debts = new ObservableArrayList<>();
    public final ObservableList<Card> cards = new ObservableArrayList<>();
    public final ObservableField<String> holderName = new ObservableField<>();
    public final ObservableBoolean showImage = new ObservableBoolean();

    public void setShowImage(boolean show) {
        showImage.set(show);
    }

    public void setHolderName(String name) {
        holderName.set(name);
    }

    public void setDebts(List<Debt> items) {
        BindingUtils.setItems(debts, items);
    }

    public void setCards(List<Card> items) {
        BindingUtils.setItems(cards, items);
    }
}
