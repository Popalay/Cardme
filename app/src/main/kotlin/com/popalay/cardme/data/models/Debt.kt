package com.popalay.cardme.data.models

import com.github.nitrico.lastadapter.StableId
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Debt(
        @PrimaryKey var id: String = UUID.randomUUID().toString(),
        var message: String = "",
        var createdAt: Long = System.currentTimeMillis(),
        var isTrash: Boolean = false,
        var holder: Holder = Holder()
) : RealmObject(), StableId {

    companion object {
        const val ID = "id"
        const val CREATED_AT = "createdAt"
        const val IS_TRASH = "isTrash"
    }

    override val stableId: Long
        get() = id.hashCode().toLong()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Debt

        if (id != other.id) return false
        if (message != other.message) return false
        if (createdAt != other.createdAt) return false
        if (isTrash != other.isTrash) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + isTrash.hashCode()
        return result
    }

    override fun toString(): String {
        return "Debt(id=$id, message='$message', createdAt=$createdAt, isTrash=$isTrash)"
    }

}