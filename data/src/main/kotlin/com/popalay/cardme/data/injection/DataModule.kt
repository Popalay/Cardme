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
import com.popalay.cardme.domain.repository.CardRepository
import com.popalay.cardme.domain.repository.DebtRepository
import com.popalay.cardme.domain.repository.DeviceRepository
import com.popalay.cardme.domain.repository.HolderRepository
import com.popalay.cardme.domain.repository.SettingsRepository
import com.popalay.cardme.domain.repository.ShareRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.popalay.cardme.data.repository.CardRepository as CardRepositoryImpl
import com.popalay.cardme.data.repository.DebtRepository as DebtRepositoryImpl
import com.popalay.cardme.data.repository.HolderRepository as HolderRepositoryImpl
import com.popalay.cardme.data.repository.SettingsRepository as SettingsRepositoryImpl
import com.popalay.cardme.data.repository.device.DeviceRepository as DeviceRepositoryImpl
import com.popalay.cardme.data.repository.device.ShareRepository as ShareRepositoryImpl

@Module
abstract class DataModule {

	@Binds abstract fun bindsDeviceRepository(repository: DeviceRepositoryImpl): DeviceRepository

	@Binds abstract fun bindsCardRepository(repository: CardRepositoryImpl): CardRepository

	@Binds abstract fun bindsDebtRepository(repository: DebtRepositoryImpl): DebtRepository

	@Binds abstract fun bindsHolderRepository(repository: HolderRepositoryImpl): HolderRepository

	@Binds abstract fun bindsSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository

	@Binds abstract fun bindsShareRepository(repository: ShareRepositoryImpl): ShareRepository

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