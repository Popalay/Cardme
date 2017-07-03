package com.popalay.cardme.data.models

import android.support.annotation.DrawableRes
import android.support.annotation.IntDef
import com.github.nitrico.lastadapter.StableId
import com.popalay.cardme.R
import io.card.payment.CreditCard
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey


open class Card(
        @PrimaryKey open var id: String? = null,
        open var title: String = "",
        open var number: String = "",
        open var redactedNumber: String = "",
        open var holder: Holder = Holder(),
        @CardType open var cardType: Long = 0L,
        open var generatedBackgroundSeed: Long = 0L,
        open var position: Int = 0
) : RealmObject(), StableId {

    companion object {
        const val ID = "id"
        const val HOLDER_ID = "holder.id"
        const val FORMATTED_NUMBER = "number"
        const val POSITION = "position"

        @Retention(AnnotationRetention.SOURCE)
        @IntDef(CARD_TYPE_MAESTRO, CARD_TYPE_MASTERCARD, CARD_TYPE_VISA, CARD_TYPE_AMEX,
                CARD_TYPE_DINERSCLUB, CARD_TYPE_DISCOVER, CARD_TYPE_JCB)
        annotation class CardType

        const val CARD_TYPE_MAESTRO = 0L
        const val CARD_TYPE_MASTERCARD = 1L
        const val CARD_TYPE_VISA = 2L
        const val CARD_TYPE_AMEX = 3L
        const val CARD_TYPE_DINERSCLUB = 4L
        const val CARD_TYPE_DISCOVER = 5L
        const val CARD_TYPE_JCB = 6L
        const val CARD_TYPE_UNKNOWN = 7L
    }

    constructor(creditCard: CreditCard) : this(
            number = creditCard.cardNumber,
            redactedNumber = creditCard.redactedCardNumber,
            cardType = when (creditCard.cardType) {
                io.card.payment.CardType.MASTERCARD -> CARD_TYPE_MASTERCARD
                io.card.payment.CardType.VISA -> CARD_TYPE_VISA
                io.card.payment.CardType.MAESTRO -> CARD_TYPE_MAESTRO
                io.card.payment.CardType.AMEX -> CARD_TYPE_AMEX
                io.card.payment.CardType.DINERSCLUB -> CARD_TYPE_DINERSCLUB
                io.card.payment.CardType.DISCOVER -> CARD_TYPE_DISCOVER
                io.card.payment.CardType.JCB -> CARD_TYPE_JCB
                else -> CARD_TYPE_UNKNOWN
            }
    )

    @DrawableRes fun getIconRes() = when (cardType) {
        CARD_TYPE_MAESTRO -> R.drawable.ic_maestro
        CARD_TYPE_MASTERCARD -> R.drawable.ic_mastercard
        CARD_TYPE_VISA -> R.drawable.ic_visa
        CARD_TYPE_AMEX -> R.drawable.ic_amex
        CARD_TYPE_DINERSCLUB -> R.drawable.ic_diners_club
        CARD_TYPE_DISCOVER -> R.drawable.ic_discover
        CARD_TYPE_JCB -> R.drawable.ic_jcb
        else -> R.drawable.ic_unknown
    }

    override val stableId: Long
        get() = id?.hashCode()?.toLong() ?: 0L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as Card
        if (id != other.id) return false
        if (title != other.title) return false
        if (number != other.number) return false
        if (redactedNumber != other.redactedNumber) return false
        if (holder != other.holder) return false
        if (cardType != other.cardType) return false
        if (generatedBackgroundSeed != other.generatedBackgroundSeed) return false
        if (position != other.position) return false
        return true
    }


    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + title.hashCode()
        result = 31 * result + number.hashCode()
        result = 31 * result + redactedNumber.hashCode()
        result = 31 * result + holder.hashCode()
        result = 31 * result + cardType.hashCode()
        result = 31 * result + generatedBackgroundSeed.hashCode()
        result = 31 * result + position
        return result
    }
}
