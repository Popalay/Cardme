package com.popalay.cardme.data.models

import com.github.nitrico.lastadapter.StableId
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Holder(
        @PrimaryKey open var name: String = "",
        open var cardsCount: Int = 0,
        open var debtCount: Int = 0,
        open var isTrash: Boolean = false
) : RealmObject(), StableId {

    companion object {
        const val NAME = "name"
        const val CARDS_COUNT = "cardsCount"
        const val DEBTS_COUNT = "debtCount"
        const val IS_TRASH = "isTrash"
    }

    override val stableId: Long
        get() = name.hashCode().toLong()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Holder

        if (name != other.name) return false
        if (cardsCount != other.cardsCount) return false
        if (debtCount != other.debtCount) return false
        if (isTrash != other.isTrash) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + cardsCount
        result = 31 * result + debtCount
        result = 31 * result + isTrash.hashCode()
        return result
    }

    override fun toString() = "Holder(name='$name', cardsCount=$cardsCount, debtCount=$debtCount, isTrash=$isTrash)"

}
