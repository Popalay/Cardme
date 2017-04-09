package com.popalay.cardme.data.models;

import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;

import com.github.nitrico.lastadapter.StableId;
import com.popalay.cardme.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.card.payment.CreditCard;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Card extends RealmObject implements StableId {

    public static final String ID = "id";
    public static final String HOLDER_ID = "holder.id";
    public static final String USAGE = "usage";

    @PrimaryKey private long id;

    private String number;

    private String redactedNumber;

    private Holder holder;

    @CardType private int type;

    @CardColor private int color;

    private int usage;

    public Card() {
    }

    public Card(CreditCard creditCard) {
        this.number = creditCard.getFormattedCardNumber();
        this.redactedNumber = creditCard.getRedactedCardNumber();
        this.holder = new Holder();
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

    public Card(String number, String redactedNumber, @CardType int type) {
        this.number = number;
        this.redactedNumber = redactedNumber;
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

    public Holder getHolder() {
        return holder;
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
    }

    @CardType
    public int getType() {
        return type;
    }

    public void setType(@CardType int type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    @DrawableRes
    public int getIconRes() {
        switch (type) {
            case CARD_TYPE_MAESTRO:
                return R.drawable.ic_maestro;
            case CARD_TYPE_MASTERCARD:
                return R.drawable.ic_mastercard;
            case CARD_TYPE_VISA:
                return R.drawable.ic_visa;
            case CARD_TYPE_AMEX:
                return R.drawable.ic_amex;
            case CARD_TYPE_DINERSCLUB:
                return R.drawable.ic_diners_club;
            case CARD_TYPE_DISCOVER:
                return R.drawable.ic_discover;
            case CARD_TYPE_JCB:
                return R.drawable.ic_jcb;
            default:
                return R.drawable.ic_unknown;
        }
    }

    @DrawableRes
    public int getColorRes() {
        switch (color) {
            case CARD_COLOR_GREEN:
                return R.drawable.bg_card_green;
            case CARD_COLOR_PURPLE:
                return R.drawable.bg_card_purple;
            case CARD_COLOR_RED:
                return R.drawable.bg_card_red;
            default:
            case CARD_COLOR_GREY:
                return R.drawable.bg_card_grey;
        }
    }

    @Override
    public long getStableId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Card card = (Card) o;

        if (id != card.id) {
            return false;
        }
        if (type != card.type) {
            return false;
        }
        if (color != card.color) {
            return false;
        }
        if (usage != card.usage) {
            return false;
        }
        if (number != null ? !number.equals(card.number) : card.number != null) {
            return false;
        }
        if (redactedNumber != null ? !redactedNumber.equals(card.redactedNumber) : card.redactedNumber != null) {
            return false;
        }
        return holder != null ? holder.equals(card.holder) : card.holder == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (redactedNumber != null ? redactedNumber.hashCode() : 0);
        result = 31 * result + (holder != null ? holder.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + color;
        result = 31 * result + usage;
        return result;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CARD_TYPE_MAESTRO, CARD_TYPE_MASTERCARD, CARD_TYPE_VISA,
            CARD_TYPE_AMEX, CARD_TYPE_DINERSCLUB, CARD_TYPE_DISCOVER, CARD_TYPE_JCB})
    public @interface CardType {}

    public static final int CARD_TYPE_MAESTRO = 0;
    public static final int CARD_TYPE_MASTERCARD = 1;
    public static final int CARD_TYPE_VISA = 2;
    public static final int CARD_TYPE_AMEX = 3;
    public static final int CARD_TYPE_DINERSCLUB = 4;
    public static final int CARD_TYPE_DISCOVER = 5;
    public static final int CARD_TYPE_JCB = 6;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CARD_COLOR_GREY, CARD_COLOR_RED, CARD_COLOR_GREEN, CARD_COLOR_PURPLE})
    public @interface CardColor {}

    public static final int CARD_COLOR_GREY = 0;
    public static final int CARD_COLOR_RED = 1;
    public static final int CARD_COLOR_GREEN = 2;
    public static final int CARD_COLOR_PURPLE = 3;

}
