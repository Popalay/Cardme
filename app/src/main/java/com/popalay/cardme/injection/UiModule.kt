package com.popalay.cardme.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.popalay.cardme.presentation.base.navigation.ViewModelFactory
import com.popalay.cardme.presentation.screens.addcard.AddCardActivity
import com.popalay.cardme.presentation.screens.addcard.AddCardModule
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsActivity
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsModule
import com.popalay.cardme.presentation.screens.holders.HoldersViewModel
import com.popalay.cardme.presentation.screens.home.HomeActivity
import com.popalay.cardme.presentation.screens.home.HomeModule
import com.popalay.cardme.presentation.screens.settings.SettingsActivity
import com.popalay.cardme.presentation.screens.settings.SettingsModule
import com.popalay.cardme.presentation.screens.splash.SplashActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class UiModule {

    //FIXME move to HoldersModule when fix dagger issue
    @Binds
    @IntoMap
    @ViewModelKey(HoldersViewModel::class)
    abstract fun bindsHoldersViewModel(viewModel: HoldersViewModel): ViewModel

    @Binds
    abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector(modules = arrayOf(AddCardModule::class))
    abstract fun contributeAddCardActivity(): AddCardActivity

    @ContributesAndroidInjector(modules = arrayOf(HomeModule::class))
    abstract fun contributeHomeActivity(): HomeActivity

    @ContributesAndroidInjector(modules = arrayOf(HolderDetailsModule::class))
    abstract fun contributeHolderDetailsActivity(): HolderDetailsActivity

    @ContributesAndroidInjector(modules = arrayOf(SettingsModule::class))
    abstract fun contributeSettingsActivity(): SettingsActivity

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

}