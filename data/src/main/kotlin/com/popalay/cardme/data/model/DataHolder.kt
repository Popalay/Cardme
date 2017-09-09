package com.popalay.cardme.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "holders",
        indices = arrayOf(Index(value = "name", unique = true)))
data class DataHolder(
        @PrimaryKey var name: String,
        var isTrash: Boolean,
        var isPending: Boolean
)