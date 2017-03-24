package com.popalay.yocard.ui.adddebt;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.data.models.Holder;

public class AddDebtViewModel {

    public final ObservableField<String> to = new ObservableField<>();
    public final ObservableField<String> message = new ObservableField<>();
    public final ObservableBoolean canSave = new ObservableBoolean(false);

    public final Debt debt;

    public AddDebtViewModel(Debt debt) {
        this.debt = debt;

        to.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                debt.getHolder().setName(to.get());
                updateCanSave();
            }
        });

        message.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                debt.setMessage(message.get());
                updateCanSave();
            }
        });
    }

    public AddDebtViewModel() {
        this(new Debt());
        debt.setHolder(new Holder());
    }

    private void updateCanSave() {
        canSave.set(!TextUtils.isEmpty(to.get()) && !TextUtils.isEmpty(message.get()));
    }
}
