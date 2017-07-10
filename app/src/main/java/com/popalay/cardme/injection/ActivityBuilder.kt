package com.popalay.cardme.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.popalay.cardme.presentation.base.navigation.ViewModelFactory
import com.popalay.cardme.presentation.screens.addcard.AddCardActivity
import com.popalay.cardme.presentation.screens.addcard.AddCardModule
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsViewModel
import com.popalay.cardme.presentation.screens.home.HomeActivity
import com.popalay.cardme.presentation.screens.home.HomeModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ActivityBuilder {

    @Binds
    abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HolderDetailsViewModel::class)
    abstract fun bindsHolderDetailsViewModel(holderDetailsViewModel: HolderDetailsViewModel): ViewModel

    @ContributesAndroidInjector(modules = arrayOf(AddCardModule::class))
    abstract fun contributeAddCardActivity(): AddCardActivity

    @ContributesAndroidInjector(modules = arrayOf(HomeModule::class))
    abstract fun contributeHomeActivity(): HomeActivity

}