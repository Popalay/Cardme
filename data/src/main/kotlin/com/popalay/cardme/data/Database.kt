package com.popalay.cardme.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.popalay.cardme.data.dao.CardDao
import com.popalay.cardme.data.models.Card

@Database(entities = arrayOf(Card::class), version = 1)
abstract class Database : RoomDatabase() {

    abstract fun cardDao(): CardDao
}