package com.popalay.cardme.injection

import android.arch.lifecycle.ViewModelProvider
import com.popalay.cardme.presentation.base.ViewModelFactory
import com.popalay.cardme.presentation.screens.addcard.AddCardActivity
import com.popalay.cardme.presentation.screens.addcard.AddCardModule
import com.popalay.cardme.presentation.screens.adddebt.AddDebtActivity
import com.popalay.cardme.presentation.screens.adddebt.AddDebtModule
import com.popalay.cardme.presentation.screens.carddetails.CardDetailsActivity
import com.popalay.cardme.presentation.screens.carddetails.CardDetailsModule
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsActivity
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsModule
import com.popalay.cardme.presentation.screens.home.HomeActivity
import com.popalay.cardme.presentation.screens.home.HomeModule
import com.popalay.cardme.presentation.screens.settings.SettingsActivity
import com.popalay.cardme.presentation.screens.settings.SettingsModule
import com.popalay.cardme.presentation.screens.splash.SplashActivity
import com.popalay.cardme.presentation.screens.trash.TrashActivity
import com.popalay.cardme.presentation.screens.trash.TrashModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UiModule {

    @Binds
    abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @PerActivity
    @ContributesAndroidInjector()
    abstract fun contributeSplashActivity(): SplashActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(AddCardModule::class))
    abstract fun contributeAddCardActivity(): AddCardActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(HomeModule::class))
    abstract fun contributeHomeActivity(): HomeActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(HolderDetailsModule::class))
    abstract fun contributeHolderDetailsActivity(): HolderDetailsActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(SettingsModule::class))
    abstract fun contributeSettingsActivity(): SettingsActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(AddDebtModule::class))
    abstract fun contributeAddDebtActivity(): AddDebtActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(TrashModule::class))
    abstract fun contributeTrashActivity(): TrashActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(CardDetailsModule::class))
    abstract fun contributeCardDetailsActivity(): CardDetailsActivity

}