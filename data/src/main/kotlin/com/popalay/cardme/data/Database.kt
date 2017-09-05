package com.popalay.cardme.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.popalay.cardme.data.dao.CardDao
import com.popalay.cardme.data.model.DataCard

@Database(entities = arrayOf(DataCard::class), version = 1)
abstract class Database : RoomDatabase() {

    abstract fun cardDao(): CardDao
}