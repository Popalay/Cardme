package com.popalay.cardme.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.popalay.cardme.data.dao.CardDao
import com.popalay.cardme.data.dao.HolderDao
import com.popalay.cardme.data.dao.SettingsDao
import com.popalay.cardme.data.model.DataCard
import com.popalay.cardme.data.model.DataDebt
import com.popalay.cardme.data.model.DataHolder
import com.popalay.cardme.data.model.DataSettings

@Database(entities = arrayOf(
        DataCard::class,
        DataHolder::class,
        DataDebt::class,
        DataSettings::class
), version = 1)
abstract class Database : RoomDatabase() {

    abstract fun cardDao(): CardDao

    abstract fun holderDao(): HolderDao

    abstract fun settingsDao(): SettingsDao

}