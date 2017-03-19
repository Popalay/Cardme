package com.popalay.yocard.utils.databinding;

import android.databinding.BaseObservable;

public class ObservableString extends BaseObservable {

    private String value = "";

    public ObservableString(String value) {
        this.value = value;
    }

    public ObservableString() { }

    public String get() {
        return value != null ? value : "";
    }

    public void set(String value) {
        if (value == null) {
            value = "";
        }
        if (!this.value.contentEquals(value)) {
            this.value = value;
            notifyChange();
        }
    }

    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    public void clear() { set(null); }
}