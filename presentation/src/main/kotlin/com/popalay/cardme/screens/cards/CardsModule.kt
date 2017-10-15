package com.popalay.cardme.screens.cards

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.PerFragment
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class CardsModule {

    @Binds
    @IntoMap
    @PerFragment
    @ViewModelKey(CardsViewModel::class)
    abstract fun bindsCardsViewModel(viewModel: CardsViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @PerFragment
        @JvmStatic fun provideViewModelFacade(viewModel: CardsViewModel): CardsViewModelFacade = viewModel
    }
}