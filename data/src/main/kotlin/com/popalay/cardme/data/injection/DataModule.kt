package com.popalay.cardme.data.injection

import android.arch.persistence.room.Room
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.popalay.cardme.data.Database
import com.popalay.cardme.data.dao.CardDao
import com.popalay.cardme.data.repository.DataCardRepository
import com.popalay.cardme.data.repository.DataDebtRepository
import com.popalay.cardme.data.repository.DataHolderRepository
import com.popalay.cardme.data.repository.DataSettingsRepository
import com.popalay.cardme.data.repository.device.DataDeviceRepository
import com.popalay.cardme.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindsRepositiory(repository: DataDeviceRepository): DeviceRepository

    @Binds
    @Singleton
    abstract fun bindsRepositiory(repository: DataCardRepository): CardRepository

    @Binds
    @Singleton
    abstract fun bindsRepositiory(repository: DataDebtRepository): DebtRepository

    @Binds
    @Singleton
    abstract fun bindsRepositiory(repository: DataHolderRepository): HolderRepository

    @Binds
    @Singleton
    abstract fun bindsRepositiory(repository: DataSettingsRepository): SettingsRepository

    @Module
    companion object {

        @Provides
        @Singleton
        fun provideGson(): Gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()

        @Provides
        @Singleton
        fun provideDatabase(context: Context): Database
                = Room.databaseBuilder(context, Database::class.java, "cardme-db").build()

        @Provides
        @Singleton
        fun provideCardDao(database: Database): CardDao = database.cardDao()
    }

}