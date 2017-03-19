package com.popalay.yocard.data.models;

import io.realm.RealmObject;

public class Debt extends RealmObject {

    private Holder holder;

    private String message;

    public Debt() {
    }

    public Debt(Holder holder, String message) {
        this.holder = holder;
        this.message = message;
    }

    public Holder getHolder() {
        return holder;
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
