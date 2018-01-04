package com.popalay.cardme.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "debts",
        indices = [Index("id", unique = true), Index("holderName")])
data class Debt(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val message: String,
        val createdAt: Long,
        val isTrash: Boolean,
        val holderName: String
)