package com.popalay.cardme.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "holders",
        indices = arrayOf(Index(value = "name", unique = true)))
data class DataHolder(
        @Expose @PrimaryKey var name: String,
        @Expose var isTrash: Boolean
) : StableId {

    override val stableId: Long
        get() = name.hashCode().toLong()
}
