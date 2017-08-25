package com.popalay.cardme.presentation.screens.settings

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.PerActivity
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SettingsModule {

    @Binds
    @IntoMap
    @PerActivity
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindsSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}
