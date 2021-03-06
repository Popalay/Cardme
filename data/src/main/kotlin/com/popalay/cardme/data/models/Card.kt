package com.popalay.cardme.data.models

import android.support.annotation.DrawableRes
import android.support.annotation.IntDef
import com.google.gson.annotations.Expose
import com.popalay.cardme.data.R
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Card(
        @Expose @PrimaryKey var number: String = "",
        @Expose var title: String = "",
        @Expose var redactedNumber: String = "",
        @Expose @field:CardType var cardType: Long = 0L,
        @Expose var generatedBackgroundSeed: Long = System.nanoTime(),
        @Expose var position: Int = 0,
        @Expose var isTrash: Boolean = false,
        @Expose var holder: Holder = Holder()
) : RealmObject(), StableId {

    companion object {
        const val NUMBER = "number"
        const val HOLDER_NAME = "holder.name"
        const val POSITION = "position"
        const val IS_TRASH = "isTrash"

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
        get() = number.hashCode().toLong()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Card

        if (number != other.number) return false
        if (title != other.title) return false
        if (redactedNumber != other.redactedNumber) return false
        if (cardType != other.cardType) return false
        if (generatedBackgroundSeed != other.generatedBackgroundSeed) return false
        if (position != other.position) return false
        if (isTrash != other.isTrash) return false

        return true
    }

    override fun hashCode(): Int {
        var result = number.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + redactedNumber.hashCode()
        result = 31 * result + cardType.hashCode()
        result = 31 * result + generatedBackgroundSeed.hashCode()
        result = 31 * result + position
        result = 31 * result + isTrash.hashCode()
        return result
    }

    override fun toString() = "Card(number='$number', title='$title', redactedNumber='$redactedNumber', cardType=$cardType, generatedBackgroundSeed=$generatedBackgroundSeed, position=$position, isTrash=$isTrash)"

}