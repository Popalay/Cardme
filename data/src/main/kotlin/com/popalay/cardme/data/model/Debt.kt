package com.popalay.cardme.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "debts",
        indices = [Index("holderName")],
        foreignKeys = [ForeignKey(entity = Holder::class,
                parentColumns = ["name"],
                childColumns = ["holderName"],
                onDelete = ForeignKey.CASCADE)])
data class Debt(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val message: String,
        val createdAt: Long,
        val isTrash: Boolean,
        val holderName: String
)