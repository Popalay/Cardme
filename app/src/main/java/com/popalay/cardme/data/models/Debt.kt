package com.popalay.cardme.data.models

import com.github.nitrico.lastadapter.StableId
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Debt(
        @PrimaryKey open var id: String? = null,
        open var holder: Holder = Holder(),
        open var message: String = "",
        open var createdAt: Long = 0
) : RealmObject(), StableId {

    companion object {
        const val ID = "id"
        const val CREATED_AT = "createdAt"
        const val HOLDER_ID = "holder.id"
    }

    override val stableId: Long
        get() = id?.hashCode()?.toLong() ?: 0L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as Debt
        if (id != other.id) return false
        if (holder != other.holder) return false
        if (message != other.message) return false
        if (createdAt != other.createdAt) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + holder.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}