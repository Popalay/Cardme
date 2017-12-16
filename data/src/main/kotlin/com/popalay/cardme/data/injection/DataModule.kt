package com.popalay.cardme.data.injection

import android.arch.persistence.room.Room
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.popalay.cardme.data.Database
import com.popalay.cardme.data.dao.CardDao
import com.popalay.cardme.data.dao.DebtDao
import com.popalay.cardme.data.dao.HolderDao
import com.popalay.cardme.data.dao.SettingsDao
import com.popalay.cardme.data.repository.DataCardRepository
import com.popalay.cardme.data.repository.DataDebtRepository
import com.popalay.cardme.data.repository.DataHolderRepository
import com.popalay.cardme.data.repository.DataSettingsRepository
import com.popalay.cardme.data.repository.device.DataDeviceRepository
import com.popalay.cardme.domain.repository.CardRepository
import com.popalay.cardme.domain.repository.DebtRepository
import com.popalay.cardme.domain.repository.DeviceRepository
import com.popalay.cardme.domain.repository.HolderRepository
import com.popalay.cardme.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Binds @Singleton
    abstract fun bindsDeviceRepositoryRepositiory(repository: DataDeviceRepository): DeviceRepository

    @Binds @Singleton
    abstract fun bindsCardRepositoryRepositiory(repository: DataCardRepository): CardRepository

    @Binds @Singleton
    abstract fun bindsDebtRepositoryRepositiory(repository: DataDebtRepository): DebtRepository

    @Binds @Singleton
    abstract fun bindsHolderRepositoryRepositiory(repository: DataHolderRepository): HolderRepository

    @Binds @Singleton
    abstract fun bindsSettingsRepositoryRepositiory(repository: DataSettingsRepository): SettingsRepository

    @Module
    companion object {

        @Provides @Singleton @JvmStatic
        fun provideGson(): Gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()

        @Provides @Singleton @JvmStatic
        fun provideDatabase(context: Context): Database =
                Room.databaseBuilder(context, Database::class.java, "cardme-db")
                        .fallbackToDestructiveMigration()
                        .build()

        @Provides @JvmStatic
        fun provideCardDao(database: Database): CardDao = database.cardDao()

        @Provides @JvmStatic
        fun provideDebtDao(database: Database): DebtDao = database.debtDao()

        @Provides @JvmStatic
        fun provideHolderDao(database: Database): HolderDao = database.holderDao()

        @Provides @JvmStatic
        fun provideSettingsDao(database: Database): SettingsDao = database.settingsDao()
    }

}