package com.popalay.cardme.presentation.screens.settings

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.ViewModelKey
import com.popalay.cardme.presentation.screens.holders.HoldersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SettingsModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindsSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}
