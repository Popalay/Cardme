package com.popalay.cardme.screens.home

import android.arch.lifecycle.ViewModel
import com.alexfacciorusso.daggerviewmodel.ViewModelKey
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.injection.PerActivity
import com.popalay.cardme.injection.PerFragment
import com.popalay.cardme.screens.cards.CardsFragment
import com.popalay.cardme.screens.cards.CardsModule
import com.popalay.cardme.screens.debts.DebtsFragment
import com.popalay.cardme.screens.debts.DebtsModule
import com.popalay.cardme.screens.holders.HoldersFragment
import com.popalay.cardme.screens.holders.HoldersModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class HomeModule {

    @Binds
    @PerActivity
    abstract fun bindsNavigator(navigator: HomeNavigator): CustomNavigator

    @PerFragment
    @ContributesAndroidInjector(modules = arrayOf(CardsModule::class))
    abstract fun contributeCardsFragment(): CardsFragment

    @PerFragment
    @ContributesAndroidInjector(modules = arrayOf(HoldersModule::class))
    abstract fun contributeHoldersFragment(): HoldersFragment

    @PerFragment
    @ContributesAndroidInjector(modules = arrayOf(DebtsModule::class))
    abstract fun contributeDebtsFragment(): DebtsFragment

    @Binds
    @IntoMap
    @PerActivity
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindsHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @PerActivity
        @JvmStatic fun provideViewModelFacade(viewModel: HomeViewModel): HomeViewModelFacade = viewModel
    }
}