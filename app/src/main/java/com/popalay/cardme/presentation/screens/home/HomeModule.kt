package com.popalay.cardme.presentation.screens.home

import com.popalay.cardme.presentation.screens.cards.CardsFragment
import com.popalay.cardme.presentation.screens.debts.DebtsFragment
import com.popalay.cardme.presentation.screens.holders.HoldersFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeModule {

    @ContributesAndroidInjector
    abstract fun contributeCardsFragment(): CardsFragment

    @ContributesAndroidInjector
    abstract fun contributeHoldersFragment(): HoldersFragment

    @ContributesAndroidInjector
    abstract fun contributeDebtsFragment(): DebtsFragment
}