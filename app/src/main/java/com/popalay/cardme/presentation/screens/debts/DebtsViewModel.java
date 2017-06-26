package com.popalay.cardme.presentation.screens.debts;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.utils.BindingUtils;

import java.util.List;

public class DebtsViewModel {

    public final ObservableList<Debt> debts = new ObservableArrayList<>();

    public void setDebts(List<Debt> items) {
        BindingUtils.setItems(debts, items);
    }

    public Debt get(int position) {
        return debts.get(position);
    }

}
