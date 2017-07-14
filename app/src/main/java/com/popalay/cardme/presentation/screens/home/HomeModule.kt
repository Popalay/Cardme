package com.popalay.cardme.presentation.screens.home

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.ViewModelKey
import com.popalay.cardme.presentation.screens.cards.CardsFragment
import com.popalay.cardme.presentation.screens.cards.CardsModule
import com.popalay.cardme.presentation.screens.debts.DebtsFragment
import com.popalay.cardme.presentation.screens.debts.DebtsModule
import com.popalay.cardme.presentation.screens.holders.HoldersFragment
import com.popalay.cardme.presentation.screens.holders.HoldersModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class HomeModule {

    @ContributesAndroidInjector(modules = arrayOf(CardsModule::class))
    abstract fun contributeCardsFragment(): CardsFragment

    @ContributesAndroidInjector(modules = arrayOf(HoldersModule::class))
    abstract fun contributeHoldersFragment(): HoldersFragment

    @ContributesAndroidInjector(modules = arrayOf(DebtsModule::class))
    abstract fun contributeDebtsFragment(): DebtsFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindsHomeViewModel(viewModel: HomeViewModel): ViewModel
}