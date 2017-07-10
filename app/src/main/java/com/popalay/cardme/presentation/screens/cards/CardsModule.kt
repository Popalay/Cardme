package com.popalay.cardme.presentation.screens.cards

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CardsModule {

    @Binds
    @IntoMap
    @ViewModelKey(CardsViewModel::class)
    abstract fun bindsCardsViewModel(viewModel: CardsViewModel): ViewModel
}