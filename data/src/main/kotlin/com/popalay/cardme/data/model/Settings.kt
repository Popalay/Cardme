package com.popalay.cardme.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
        @PrimaryKey val id: Long,
        val language: String,
        val theme: String,
        val isCardBackground: Boolean = false
)
