package com.popalay.cardme.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "debts",
        indices = arrayOf(Index(value = "holderName")),
        foreignKeys = arrayOf(ForeignKey(entity = DataHolder::class,
                parentColumns = arrayOf("name"),
                childColumns = arrayOf("holderName"),
                onDelete = ForeignKey.CASCADE)))
data class DataDebt(
        @Expose @PrimaryKey(autoGenerate = true) var id: Long,
        @Expose var message: String,
        @Expose var createdAt: Long,
        @Expose var isTrash: Boolean,
        @Expose var holderName: String
) : StableId {

    override val stableId: Long
        get() = id
}