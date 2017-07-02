package com.popalay.cardme.presentation.screens.addcard;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.popalay.cardme.data.models.Card;

public class AddCardViewModel {

    public final ObservableField<String> holderName = new ObservableField<>();
    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableBoolean canSave = new ObservableBoolean();
    public final ObservableBoolean showImage = new ObservableBoolean();
    public final ObservableField<Card> card = new ObservableField<>();

    public AddCardViewModel() {
        holderName.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (card.get() == null) return;
                card.get().getHolder().setName(holderName.get());
                updateCanSave();
            }
        });
        title.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (card.get() == null) return;
                card.get().setTitle(title.get());
            }
        });
    }

    public void setShowImage(boolean show) {
        showImage.set(show);
    }

    public void setCard(Card card) {
        this.card.set(card);
    }

    private void updateCanSave() {
        canSave.set(!TextUtils.isEmpty(holderName.get().trim()));
    }
}
