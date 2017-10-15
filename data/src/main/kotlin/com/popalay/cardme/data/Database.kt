package com.popalay.cardme.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.popalay.cardme.data.dao.CardDao
import com.popalay.cardme.data.dao.HolderDao
import com.popalay.cardme.data.dao.SettingsDao
import com.popalay.cardme.data.model.Card
import com.popalay.cardme.data.model.Debt
import com.popalay.cardme.data.model.Holder
import com.popalay.cardme.data.model.Settings

@Database(entities = [(Card::class), (Holder::class), (Debt::class), (Settings::class)], version = 1)
abstract class Database : RoomDatabase() {

    abstract fun cardDao(): CardDao

    abstract fun holderDao(): HolderDao

    abstract fun settingsDao(): SettingsDao

}