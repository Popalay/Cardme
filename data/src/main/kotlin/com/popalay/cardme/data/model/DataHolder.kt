package com.popalay.cardme.data.model

import com.google.gson.annotations.Expose
import com.popalay.cardme.domain.model.Holder
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DataHolder(
        @Expose @PrimaryKey var name: String = "",
        @Expose var isTrash: Boolean = false,
        var debts: RealmList<DataDebt> = RealmList()
) : RealmObject(), StableId {

    companion object {
        const val NAME = "name"
        const val CARDS = "cards"
        const val DEBTS = "debts"
        const val IS_TRASH = "isTrash"
    }

    override val stableId: Long
        get() = name.hashCode().toLong()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Holder

        if (name != other.name) return false
        if (isTrash != other.isTrash) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + isTrash.hashCode()
        return result
    }

    override fun toString() = "DataHolder(name='$name', isTrash=$isTrash)"

}
