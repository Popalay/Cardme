package com.popalay.yocard.data.models;

import android.support.annotation.IntDef;

import com.popalay.yocard.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.card.payment.CreditCard;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Card extends RealmObject {

    @PrimaryKey private long id;

    private String number;

    private String redactedNumber;

    private String holderName;

    @CardType private int type;

    public Card() {
    }

    public Card(CreditCard creditCard) {
        this.number = creditCard.getFormattedCardNumber();
        this.redactedNumber = creditCard.getRedactedCardNumber();
        switch (creditCard.getCardType()) {
            case MASTERCARD:
                this.type = CARD_TYPE_MASTERCARD;
                break;
            case VISA:
                this.type = CARD_TYPE_VISA;
                break;
            case MAESTRO:
                this.type = CARD_TYPE_MAESTRO;
                break;
        }
    }

    public Card(String number, String redactedNumber, String holderName, @CardType int type) {
        this.number = number;
        this.redactedNumber = redactedNumber;
        this.holderName = holderName;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRedactedNumber() {
        return redactedNumber;
    }

    public void setRedactedNumber(String redactedNumber) {
        this.redactedNumber = redactedNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    @CardType
    public int getType() {
        return type;
    }

    public void setType(@CardType int type) {
        this.type = type;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CARD_TYPE_MAESTRO, CARD_TYPE_MASTERCARD, CARD_TYPE_VISA})
    public @interface CardType {}

    public static final int CARD_TYPE_MAESTRO = R.drawable.ic_maestro;
    public static final int CARD_TYPE_MASTERCARD = R.drawable.ic_mastercard;
    public static final int CARD_TYPE_VISA = R.drawable.ic_visa;
}
