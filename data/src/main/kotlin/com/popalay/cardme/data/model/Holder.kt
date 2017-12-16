package com.popalay.cardme.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "holders",
        indices = [Index("name", unique = true)])
data class Holder(
        @PrimaryKey val name: String,
        val isTrash: Boolean
)