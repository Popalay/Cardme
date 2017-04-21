package com.popalay.cardme.ui.addcard;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.popalay.cardme.data.models.Card;

public class AddCardViewModel {

    public final ObservableField<String> holderName = new ObservableField<>();
    public final ObservableBoolean canSave = new ObservableBoolean();

    public final Card card;

    public AddCardViewModel(Card card) {
        this.card = card;

        holderName.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                card.getHolder().setName(holderName.get());
                updateCanSave();
            }
        });
    }

    private void updateCanSave() {
        canSave.set(!TextUtils.isEmpty(holderName.get().trim()));
    }
}
