package com.popalay.cardme.presentation.screens.holders;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.popalay.cardme.data.models.Holder;
import com.popalay.cardme.utils.BindingUtils;

import java.util.List;

public class HoldersViewModel {

    public final ObservableList<Holder> holders = new ObservableArrayList<>();

    public void setHolders(List<Holder> items) {
        BindingUtils.setItems(holders, items);
    }

}
