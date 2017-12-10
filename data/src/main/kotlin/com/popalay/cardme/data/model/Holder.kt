package com.popalay.cardme.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "holders",
        indices = [(Index("name", unique = true))])
data class Holder(
        @PrimaryKey var name: String,
        var isTrash: Boolean
)