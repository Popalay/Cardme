package com.popalay.cardme.screens.carddetails

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.injection.PerActivity
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CardDetailsModule {

    @Binds @PerActivity
    fun bindsNavigator(navigator: CardDetailsNavigator): CustomNavigator

    @Binds @IntoMap @PerActivity
    @ViewModelKey(CardDetailsViewModel::class)
    fun bindsCardDetailsViewModel(viewModel: CardDetailsViewModel): ViewModel
}