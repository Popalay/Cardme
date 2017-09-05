package com.popalay.cardme.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.DrawableRes
import android.support.annotation.IntDef
import com.google.gson.annotations.Expose
import com.popalay.cardme.data.R

@Entity(tableName = "cards",
        indices = arrayOf(Index(value = "number", unique = true))/*,
        foreignKeys = arrayOf(ForeignKey(entity = DataHolder::class,
                parentColumns = arrayOf("name"),
                childColumns = arrayOf("holderName"),
                onDelete = ForeignKey.CASCADE))*/
) data class DataCard(
        @Expose @PrimaryKey var number: String = "",
        @Expose var title: String = "",
        @Expose var redactedNumber: String = "",
        @Expose @CardType val cardType: Long,
        @Expose var generatedBackgroundSeed: Long = System.nanoTime(),
        @Expose var position: Int = 0,
        @Expose var isTrash: Boolean = false,
        @Expose var holderName: String = ""
) : StableId {

    companion object {
        @IntDef(CARD_TYPE_MAESTRO, CARD_TYPE_MASTERCARD, CARD_TYPE_VISA, CARD_TYPE_AMEX,
                CARD_TYPE_DINERSCLUB, CARD_TYPE_DISCOVER, CARD_TYPE_JCB, CARD_TYPE_UNKNOWN)
        @Retention(AnnotationRetention.SOURCE)
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

    @DrawableRes
    fun getIconRes() = when (cardType) {
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

}