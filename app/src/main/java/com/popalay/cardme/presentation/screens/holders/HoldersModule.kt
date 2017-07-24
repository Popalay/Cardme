package com.popalay.cardme.presentation.screens.holders

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HoldersModule {

    @Binds
    @IntoMap
    @ViewModelKey(HoldersViewModel::class)
    abstract fun bindsHoldersViewModel(viewModel: HoldersViewModel): ViewModel
}
