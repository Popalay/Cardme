package com.popalay.yocard.ui.adddebt.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.popalay.yocard.BR;
import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.data.models.Holder;

public class AddDebtViewModel extends BaseObservable {

    private Debt debt;

    public AddDebtViewModel() {
        this.debt = new Debt();
        debt.setHolder(new Holder());
    }

    public AddDebtViewModel(Debt debt) {
        this.debt = debt;
    }

    public Debt getDebt() {
        return debt;
    }

    public void setDebt(Debt debt) {
        this.debt = debt;
    }

    @Bindable
    public String getTo() {
        return debt.getHolder().getName();
    }

    public void setTo(String to) {
        debt.getHolder().setName(to);
        notifyPropertyChanged(BR.to);
    }

    @Bindable
    public String getMessage() {
        return debt.getMessage();
    }

    public void setMessage(String message) {
        debt.setMessage(message);
        notifyPropertyChanged(BR.message);
    }
}
