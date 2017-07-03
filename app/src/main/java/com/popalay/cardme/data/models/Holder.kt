package com.popalay.cardme.data.models

import com.github.nitrico.lastadapter.StableId
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Holder(
        @PrimaryKey open var id: String? = null,
        open var name: String = "",
        open var cardsCount: Int = 0,
        open var debtCount: Int = 0
) : RealmObject(), StableId {

    companion object {
        const val ID = "id"
        const val NAME = "name"
        const val CARDS_COUNT = "cardsCount"
        const val DEBTS_COUNT = "debtCount"
    }

    override val stableId: Long
        get() = id?.hashCode()?.toLong() ?: 0L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as Holder
        if (id != other.id) return false
        if (name != other.name) return false
        if (cardsCount != other.cardsCount) return false
        if (debtCount != other.debtCount) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + cardsCount
        result = 31 * result + debtCount
        return result
    }
}
