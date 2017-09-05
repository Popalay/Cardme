package com.popalay.cardme.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "settings")
data class DataSettings(
        @PrimaryKey(autoGenerate = true) var id: Long,
        var language: String = "",
        var theme: String = "",
        var isCardBackground: Boolean = false
)
